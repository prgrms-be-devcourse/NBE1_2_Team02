package com.example.book_your_seat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Configuration
public class WebClientConfig {

    private static final String SECRET_KEY = "yourSecretKey";

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // Base64 인코딩된 인증 정보 생성
        String auth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());

        return builder
                .baseUrl("https://api.tosspayments.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + auth)
                .build();
    }
}
