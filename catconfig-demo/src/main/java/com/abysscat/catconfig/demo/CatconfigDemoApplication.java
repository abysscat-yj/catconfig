package com.abysscat.catconfig.demo;

import com.abysscat.catconfig.client.annocation.EnableCatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@SpringBootApplication
@EnableConfigurationProperties({CatDemoConfig.class})
@EnableCatConfig
@RestController
public class CatconfigDemoApplication {

	@Value("${cat.a}-${cat.b}")
	private String a;

	@Value("${cat.b}")
	private String b;

	@Autowired
	private CatDemoConfig demoConfig;

	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(CatconfigDemoApplication.class, args);
	}

	@GetMapping("/demo")
	public String demo() {
		return "cat.a = " + a + "\n"
				+ "cat.b = " + b + "\n"
				+ "demo.a = " + demoConfig.getA() + "\n"
				+ "demo.b = " + demoConfig.getB() + "\n";
	}

	@Bean
	ApplicationRunner applicationRunner() {
		System.out.println(Arrays.toString(environment.getActiveProfiles()));
		return args -> {
			System.out.println(a);
			System.out.println(demoConfig.getA());
		};
	}

}
