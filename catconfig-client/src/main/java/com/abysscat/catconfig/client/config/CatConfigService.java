package com.abysscat.catconfig.client.config;

import com.abysscat.catconfig.client.repository.CatRepository;
import com.abysscat.catconfig.client.repository.CatRepositoryImpl;

import java.util.Map;

/**
 * cat config service.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:09
 */
public interface CatConfigService {

	static CatConfigService getDefault(Map<String, String> config) {
		return new CatConfigServiceImpl(config);
	}

	String[] getPropertyNames();

	String getProperty(String name);

}
