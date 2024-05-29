package com.abysscat.catconfig.server.lock;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Distribute lock by db connection.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/29 23:30
 */
@Component
@Slf4j
public class DistributeLock {

	@Autowired
	DataSource dataSource;

	Connection connection;

	@Getter
	private AtomicBoolean locked = new AtomicBoolean(false);

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	@PostConstruct
	public void init() {
		try {
			connection = dataSource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// 每5s尝试获取锁，如果获取不到，则一直阻塞
		executor.scheduleWithFixedDelay(this::tryLock, 1000L, 5000L, TimeUnit.MILLISECONDS);
	}


	public boolean lock() throws SQLException {

		long start = System.currentTimeMillis();

		// 设置手动提交，无需释放锁
		connection.setAutoCommit(false);

		// 数据库一般默认RR级别，这里设置为RC
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		// 一般无法修改DB本身的超时时间（默认50s），这里通过事务进行设置成5s
		connection.createStatement().execute("set innodb_lock_wait_timeout = 5");

		// 获取唯一行锁，会一直持有，其他连接想要获取锁会阻塞5s， 超时会抛异常
		connection.createStatement().execute("select app from locks where id=1 for update");

		long cost = System.currentTimeMillis() - start;

		// 只有拿到锁，才会走到这里
		if (locked.get()) {
			log.debug("reentry lock, cost:{}", cost);
		} else {
			log.debug("get lock, cost:{}", cost);
		}

		return true;
	}

	private void tryLock() {
		long start = System.currentTimeMillis();
		try {
			lock();
			this.locked.set(true);
		} catch (SQLException e) {
			log.debug("tryLock failed. cost:{}", System.currentTimeMillis() - start);
			this.locked.set(false);
		}
	}

	@PreDestroy
	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.rollback();
				connection.close();
			}
		} catch (Exception e) {
			log.debug("ignore this close exception");
		}
	}

}
