package com.abysscat.catconfig.client.repository;

import com.abysscat.catconfig.client.config.ConfigMeta;
import com.abysscat.catutils.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 17:57
 */
public class CatRepositoryImpl implements CatRepository {

	ConfigMeta meta;
	Map<String, Long> versionMap = new HashMap<>();

	/**
	 * 缓存本地配置
	 */
	Map<String, Map<String, String>> configMap = new HashMap<>();

	/**
	 * 定时刷新拉取远程配置，更新到本地缓存
	 */
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	List<CatRepositoryChangeListener> listeners = new ArrayList<>();

	public CatRepositoryImpl(ConfigMeta meta) {
		this.meta = meta;
		executor.scheduleWithFixedDelay(this::heartbeat, 1000, 5000, TimeUnit.MILLISECONDS);
	}

	@Override
	public Map<String, String> getConfig() {
		String key = meta.genKey();
		if(configMap.containsKey(key)) {
			return configMap.get(key);
		}
		return findAll();
	}

	/**
	 * 拉取远程配置
	 */
	public Map<String, String> findAll() {
		String listPath = meta.listPath();
		List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});
		Map<String,String> resultMap = new HashMap<>();
		configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
		return resultMap;
	}

	@Override
	public void addListener(CatRepositoryChangeListener listener) {
		listeners.add(listener);
	}

	private void heartbeat() {
		String versionPath = meta.versionPath();
		Long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>() {});
		String key = meta.genKey();;
		Long oldVersion = versionMap.getOrDefault(key, -1L);

		// 版本号有变化，则拉取远程配置，更新到本地
		if(version > oldVersion) {
			System.out.println("need update new configs.");
			System.out.println("current version = " + version+ ", old version = " + oldVersion);

			// 更新本地版本号
			versionMap.put(key, version);

			// 拉取远程配置
			Map<String, String> newConfigs = findAll();

			// 更新本地配置
			configMap.put(key, newConfigs);

			// 发送监听事件
			listeners.forEach(l -> l.onChange(new CatRepositoryChangeListener.ChangeEvent(meta, newConfigs)));
		}
	}
}
