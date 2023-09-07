package com.example.food.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
@EnableFeignClients
public class FoodDeliveryOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryOrderApplication.class, args);
	}
}
