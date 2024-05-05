package com.abysscat.catconfig.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * Description
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:08
 */
public class CatPropertySource extends EnumerablePropertySource<CatConfigService> {

	public CatPropertySource(String name, CatConfigService source) {
		super(name, source);
	}

	@Override
	public String[] getPropertyNames() {
		return source.getPropertyNames();
	}

	@Override
	public Object getProperty(String name) {
		return source.getProperty(name);
	}

}
