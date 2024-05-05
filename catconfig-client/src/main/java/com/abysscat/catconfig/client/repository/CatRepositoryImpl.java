package com.abysscat.catconfig.client.repository;

import com.abysscat.catconfig.client.config.ConfigMeta;
import com.abysscat.catutils.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 17:57
 */
public class CatRepositoryImpl implements CatRepository {

	ConfigMeta meta;

	public CatRepositoryImpl(ConfigMeta meta) {
		this.meta = meta;
	}

	@Override
	public Map<String, String> getConfig() {
		String listPath = meta.listPath();
		List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});
		Map<String,String> resultMap = new HashMap<>();
		configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
		return resultMap;
	}
}
