package com.abysscat.catconfig.client.repository;

import com.abysscat.catconfig.client.config.CatConfigServiceImpl;
import com.abysscat.catconfig.client.config.ConfigMeta;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Description
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 17:54
 */
public interface CatRepository {

	static CatRepository getDefault(ConfigMeta meta) {
		return new CatRepositoryImpl(meta);
	}

	Map<String, String> getConfig();

}
