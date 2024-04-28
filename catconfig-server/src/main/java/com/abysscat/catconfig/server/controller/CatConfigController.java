package com.abysscat.catconfig.server.controller;

import com.abysscat.catconfig.server.mapper.ConfigsMapper;
import com.abysscat.catconfig.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CatConfigController.
 *
 * @Author: abysscat-yj
 * @Create: 2024/4/28 17:22
 */
@RestController
public class CatConfigController {

	@Autowired
	ConfigsMapper mapper;

	/**
	 * 缓存配置版本号
	 * key: app-env-ns
	 * value: 时间戳
	 */
	Map<String, Long> VERSIONS = new HashMap<>();

	@GetMapping("/list")
	public List<Configs> list(String app, String env, String ns) {
		return mapper.list(app, env, ns);
	}

	@RequestMapping("/update")
	public List<Configs> update(@RequestParam("app") String app,
								@RequestParam("env") String env,
								@RequestParam("ns") String ns,
								@RequestBody Map<String, String> params) {
		params.forEach((k, v) -> {
			insertOrUpdate(new Configs(app, env, ns, k, v));
		});
		VERSIONS.put(getVersionsMapKey(app, env, ns), System.currentTimeMillis());
		return mapper.list(app, env, ns);
	}

	private void insertOrUpdate(Configs configs) {
		Configs conf = mapper.select(configs.getApp(), configs.getEnv(),
				configs.getNs(), configs.getPkey());
		if(conf == null) {
			mapper.insert(configs);
		} else {
			mapper.update(configs);
		}
	}

	@GetMapping("/version")
	public long version(String app, String env, String ns) {
		return VERSIONS.getOrDefault(getVersionsMapKey(app, env, ns), -1L);
	}

	private String getVersionsMapKey(String app, String env, String ns) {
		return app + "-" + env + "-" + ns;
	}
}
