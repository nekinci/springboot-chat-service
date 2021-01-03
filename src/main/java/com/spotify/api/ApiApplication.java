package com.spotify.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication
@EnableCaching
public class ApiApplication {

	public static void main(String[] args) {
		System.out.println("runned at : " + new Date());
		SpringApplication.run(ApiApplication.class, args);
	}

}
