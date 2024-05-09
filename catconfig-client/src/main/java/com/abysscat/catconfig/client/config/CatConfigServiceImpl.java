package com.abysscat.catconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * cat config service impl.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:15
 */
@Slf4j
public class CatConfigServiceImpl implements CatConfigService {

	Map<String, String> config;

	ApplicationContext applicationContext;

	public CatConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
		this.applicationContext = applicationContext;
		this.config = config;
	}

	@Override
	public String[] getPropertyNames() {
		return this.config.keySet().toArray(new String[0]);
	}

	@Override
	public String getProperty(String name) {
		return this.config.get(name);
	}

	@Override
	public void onChange(ChangeEvent event) {
		// 只更新值有变化的key，避免无效更新
		Set<String> keys = selectChangedKeys(this.config, event.config());
		if(keys.isEmpty()) {
			log.info("no changed key, ignore update.");
			return;
		}
		this.config = event.config();
		if(!config.isEmpty()) {
			log.info("fire an EnvironmentChangeEvent with keys: {}", keys);
			applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
		}
	}

	private Set<String> selectChangedKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
		if(oldConfigs.isEmpty()) return newConfigs.keySet();
		if(newConfigs.isEmpty()) return oldConfigs.keySet();
		Set<String> news = newConfigs.keySet().stream()
				.filter(key -> !newConfigs.get(key).equals(oldConfigs.get(key)))
				.collect(Collectors.toSet());
		oldConfigs.keySet().stream().filter(key -> !newConfigs.containsKey(key)).forEach(news::add);
		return news;
	}
}
