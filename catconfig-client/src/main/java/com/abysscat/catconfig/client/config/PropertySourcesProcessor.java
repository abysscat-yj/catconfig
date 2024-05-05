package com.abysscat.catconfig.client.config;

import com.abysscat.catconfig.client.repository.CatRepository;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:11
 */
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, EnvironmentAware, PriorityOrdered {

	private final static String CAT_PROPERTY_SOURCES = "CatPropertySources";
	private final static String CAT_PROPERTY_SOURCE  = "CatPropertySource";

	ApplicationContext applicationContext;

	Environment environment;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ConfigurableEnvironment configurableEnv = (ConfigurableEnvironment) environment;
		if(configurableEnv.getPropertySources().contains(CAT_PROPERTY_SOURCES)) {
			return;
		}

		// 远程获取配置
		String app = configurableEnv.getProperty("catconfig.app", "app1");
		String env = configurableEnv.getProperty("catconfig.env", "dev");
		String ns = configurableEnv.getProperty("catconfig.ns", "public");
		String configServer = configurableEnv.getProperty("catconfig.configServer", "http://localhost:9129");

		ConfigMeta configMeta = new ConfigMeta(app, env, ns, configServer);

		CatRepository repository = CatRepository.getDefault(configMeta);
		Map<String, String> config = repository.getConfig();

		CatConfigService configService = CatConfigService.getDefault(config);

		CatPropertySource propertySource = new CatPropertySource(CAT_PROPERTY_SOURCE, configService);

		// 往 env 里插入组合配置
		CompositePropertySource composite = new CompositePropertySource(CAT_PROPERTY_SOURCES);
		composite.addPropertySource(propertySource);

		// 插到最前面
		configurableEnv.getPropertySources().addFirst(composite);
	}


	@Override
	public int getOrder() {
		return PriorityOrdered.HIGHEST_PRECEDENCE;
	}
}
