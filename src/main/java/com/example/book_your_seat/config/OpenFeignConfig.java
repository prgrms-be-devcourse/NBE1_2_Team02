package com.example.book_your_seat.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.example.book_your_seat")
public class OpenFeignConfig {

}
