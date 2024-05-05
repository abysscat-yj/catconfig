package com.abysscat.catconfig.client.annocation;

import com.abysscat.catconfig.client.config.CatConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * cat config client entrypoint.
 * <p>
 * 通过在 SpringBootApplication 启动类上添加该注解，导入配置中心客户端。
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 12:01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({CatConfigRegistrar.class})
public @interface EnableCatConfig {
}
