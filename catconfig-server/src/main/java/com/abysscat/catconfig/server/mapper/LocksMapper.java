package com.abysscat.catconfig.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Locks mapper for distributed lock
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/29 22:32
 */
@Repository
@Mapper
public interface LocksMapper {

	/**
	 * 这种方式难以控制锁等待时间，暂不使用
	 */
	@Select("select app from locks where id = 1 for update")
	String getLock(String key);

}
