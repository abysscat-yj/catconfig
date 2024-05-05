package com.abysscat.catconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * demo config class.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 11:59
 */
@Data
@ConfigurationProperties(prefix = "cat")
public class CatDemoConfig {

	String a;

	String b;

}
