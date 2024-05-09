package com.abysscat.catconfig.client.config;

import com.abysscat.catconfig.client.value.SpringValueProcessor;
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
		registerClass(registry, PropertySourcesProcessor.class);
		registerClass(registry, SpringValueProcessor.class);
	}

	private static void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
		System.out.println("register " + aClass.getName());
		Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
				.filter(x -> aClass.getName().equals(x)).findFirst();

		if (first.isPresent()) {
			System.out.println("PropertySourcesProcessor already registered");
			return;
		}
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
				.genericBeanDefinition(aClass).getBeanDefinition();
		registry.registerBeanDefinition(aClass.getName(), beanDefinition);
	}
}
