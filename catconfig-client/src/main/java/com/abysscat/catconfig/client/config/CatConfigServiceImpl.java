package com.abysscat.catconfig.client.config;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * cat config service impl.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:15
 */
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
		this.config = event.config();
		if (!this.config.isEmpty()) {
			System.out.println("cat config fire an EnvironmentChangeEvent with keys: " + config.keySet());
			applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
		}
	}
}
