package com.abysscat.catconfig.client.config;

/**
 * cat config service.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:09
 */
public interface CatConfigService {

	String[] getPropertyNames();

	String getProperty(String name);

}
