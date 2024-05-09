package com.abysscat.catconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * spring value.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/10 0:44
 */
@Data
@AllArgsConstructor
public class SpringValue {

	private Object bean;

	private String beanName;

	private String key;

	private String placeholder;

	private Field field;

}
