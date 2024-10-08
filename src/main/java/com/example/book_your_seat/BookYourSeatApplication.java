package com.example.book_your_seat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.book_your_seat.payment.controller")
@EnableCaching
public class BookYourSeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookYourSeatApplication.class, args);
	}

}
