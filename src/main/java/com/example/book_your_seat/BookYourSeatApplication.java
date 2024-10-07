package com.example.book_your_seat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableCaching
@SpringBootApplication
@EnableJpaAuditing

public class BookYourSeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookYourSeatApplication.class, args);
	}

}
