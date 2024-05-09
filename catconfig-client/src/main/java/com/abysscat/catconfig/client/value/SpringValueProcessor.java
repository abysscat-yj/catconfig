package com.abysscat.catconfig.client.value;

import com.abysscat.catconfig.client.util.PlaceholderHelper;
import com.abysscat.catutils.utils.FieldUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;

/**
 * process spring value.
 * <p>
 * 1. 扫描所有的 spring Value，保存起来
 * 2. 在配置变更时，更新所有的 spring value
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/10 0:45
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

	@Setter
	private BeanFactory beanFactory;

	static final PlaceholderHelper PLACEHOLDER_HELPER = PlaceholderHelper.getInstance();

	/**
	 * 用来保存所有的 spring value
	 * 之所以要用解析拆分后的 value 值作为 key， 是因为更新value时，可能存在多个包含该key的value都需要更新
	 * 比如存在 @Value("${cat.a}-${cat.b}")、@Value("${cat.a}-${cat.c}")，那么当cat.a这个key更新时，需要同时更新这两个value
	 */
	static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

	/**
	 * 重写该方法，可依次扫描所有的 bean 的 spring value
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		List<Field> springValueFields = FieldUtils.findAnnotatedField(bean.getClass(), Value.class);
		springValueFields.forEach(field -> {
			Value value = field.getAnnotation(Value.class);
			log.info("find spring value: field = {}, value = {}", field, value.value());

			// 提取所有添加@Value注解的占位符（支持SPEL解析），比如cat.a、cat.b，并保存起来
			PLACEHOLDER_HELPER.extractPlaceholderKeys(value.value()).forEach(key -> {
				log.info("find spring value: key = {}", key);
				SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
				VALUE_HOLDER.add(key, springValue);
			});
		});
		return bean;
	}

	/**
	 * 监听配置变更事件，更新所有的 spring value
	 */
	@Override
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		log.info("update spring value for keys: {}", event.getKeys());
		event.getKeys().forEach(key -> {
			log.info("update spring value: {}", key);
			List<SpringValue> springValues = VALUE_HOLDER.get(key);
			if (springValues == null || springValues.isEmpty()) {
				return;
			}
			springValues.forEach(springValue -> {
				log.info("update spring value: {} for key {}", springValue, key);
				try {
					// 通过 spring 提供的工具方法解析占位符，并更新值
					Object value = PLACEHOLDER_HELPER.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,
							springValue.getBeanName(), springValue.getPlaceholder());
					log.info("update value: {} for holder {}", value, springValue.getPlaceholder());
					springValue.getField().setAccessible(true);
					springValue.getField().set(springValue.getBean(), value);
				} catch (Exception ex) {
					log.error("update spring value error", ex);
				}
			});
		});
	}
}
