package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author PureLove1
 * EnableAsync注解开启异步调用
 * MapperScan注解mapper接口扫描
 * EnableTransactionManagement注解开启事务
 */
@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = "com.blog.mapper")
@EnableTransactionManagement
public class BlogApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(BlogApplication.class, args);
	}

}
