package com.abysscat.catconfig.client.config;

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

	public CatConfigServiceImpl(Map<String, String> config) {
		this.config = config;
	}

	@Override
	public String[] getPropertyNames() {
		return config.keySet().toArray(new String[0]);
	}

	@Override
	public String getProperty(String name) {
		return config.get(name);
	}
}
