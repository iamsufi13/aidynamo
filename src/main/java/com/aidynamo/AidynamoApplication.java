package com.aidynamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.aidynamo")
@EnableAsync
public class AidynamoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AidynamoApplication.class, args);
	}

}
