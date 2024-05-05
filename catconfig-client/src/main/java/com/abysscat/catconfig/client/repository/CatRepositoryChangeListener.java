package com.abysscat.catconfig.client.repository;

import com.abysscat.catconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 * cat repository change listener.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/6 1:49
 */
public interface CatRepositoryChangeListener {

	void onChange(ChangeEvent event);

	record ChangeEvent(ConfigMeta meta, Map<String, String> config) {}

}
