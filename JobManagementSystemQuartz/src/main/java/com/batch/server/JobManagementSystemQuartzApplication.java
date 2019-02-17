package com.batch.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.batch")
public class JobManagementSystemQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobManagementSystemQuartzApplication.class, args);
	}

}
