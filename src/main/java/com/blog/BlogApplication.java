package com.blog;

import com.blog.aop.LogRecorderAspect;
import com.blog.service.BlogService;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author PureLove1
 */
@SpringBootApplication
@MapperScan(basePackages = "com.blog.mapper")
@EnableTransactionManagement
public class BlogApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(BlogApplication.class, args);
		BlogService bean = run.getBean(BlogService.class);
		if (bean==null){
			System.out.println("blogService初始化失败");
		}
	}

}
