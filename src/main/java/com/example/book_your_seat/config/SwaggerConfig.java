package com.example.book_your_seat.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .info(new Info()
                        .title("book-your-seat API")
                        .description("book-your-seat의 API 문서입니다.")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi userAPI() {
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi couponAPI() {
        return GroupedOpenApi.builder()
                .group("Coupon API")
                .pathsToMatch("/api/v1/coupons/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminCouponAPI() {
        return GroupedOpenApi.builder()
                .group("Admin Coupon API")
                .pathsToMatch("/admin/api/v1/coupons/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reservationAPI() {
        return GroupedOpenApi.builder()
                .group("Reservation API")
                .pathsToMatch("/api/v1/reservations/**")
                .build();
    }

    @Bean
    public GroupedOpenApi seatAPI() {
        return GroupedOpenApi.builder()
                .group("Seat API")
                .pathsToMatch("/api/v1/seats/**")
                .build();
    }

    @Bean
    public GroupedOpenApi concertAPI() {
        return GroupedOpenApi.builder()
                .group("Concert API")
                .pathsToMatch("/api/v1/concerts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminConcertAPI() {
        return GroupedOpenApi.builder()
                .group("Admin Concert API")
                .pathsToMatch("/admin/api/v1/concerts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi queueAPI() {
        return GroupedOpenApi.builder()
                .group("Queue API")
                .pathsToMatch("/api/v1/queues/**")
                .build();
    }
}
