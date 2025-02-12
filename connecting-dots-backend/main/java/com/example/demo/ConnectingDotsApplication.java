package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class ConnectingDotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectingDotsApplication.class, args);
	}

}
