package com.aidynamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.aidynamo")
public class AidynamoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AidynamoApplication.class, args);
	}

}
