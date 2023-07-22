package com.blog;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author PureLove1
 * @Date 2023/7/22
 */
@SpringBootApplication
@EnableAdminServer
public class AdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class);
	}
}
