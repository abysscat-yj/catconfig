package com.abysscat.catconfig.client.config;

import com.abysscat.catconfig.client.repository.CatRepository;
import com.abysscat.catconfig.client.repository.CatRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * cat config service.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:09
 */
public interface CatConfigService extends CatRepositoryChangeListener {

	static CatConfigService getDefault(ApplicationContext applicationContext, ConfigMeta meta) {
		CatRepository repository = CatRepository.getDefault(meta);
		Map<String, String> config = repository.getConfig();
		CatConfigService configService = new CatConfigServiceImpl(applicationContext, config);

		// 将配置 Service 注册到 Repository 监听器
		repository.addListener(configService);
		return configService;
	}

	String[] getPropertyNames();

	String getProperty(String name);

}
