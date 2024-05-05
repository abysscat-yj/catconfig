package com.abysscat.catconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * register cat config bean.
 *
 * 类似 Spring Component 注解功能，此处需要手动注册
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:04
 */
@Slf4j
public class CatConfigRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//		ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);

		log.info("register cat config PropertySourcesProcessor");

		Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
				.filter(x -> PropertySourcesProcessor.class.getName().equals(x)).findFirst();

		if (first.isPresent()) {
			System.out.println("PropertySourcesProcessor already registered");
			return;
		}

		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
				.genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
		registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), beanDefinition);

	}
}
