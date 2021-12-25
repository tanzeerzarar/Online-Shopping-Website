package com.y4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.y4j"})
public class OnlineMallManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineMallManagementSystemApplication.class, args);
	}

}
