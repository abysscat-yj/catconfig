package com.abysscat.catconfig.client.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configs model.
 *
 * @Author: abysscat-yj
 * @Create: 2024/4/28 17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configs {

	private String app;

	private String env;

	private String ns;

	private String pkey;

	private String pval;

}
