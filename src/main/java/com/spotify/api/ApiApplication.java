package com.spotify.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		System.out.println("runned at : " + new Date());
		SpringApplication.run(ApiApplication.class, args);
	}

}
