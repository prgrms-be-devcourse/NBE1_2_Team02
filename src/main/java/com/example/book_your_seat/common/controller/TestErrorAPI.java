package com.example.book_your_seat.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor

public class TestErrorAPI {

    @GetMapping
    public void testError() {
        throw new IllegalArgumentException("에러 테스트");
    }

}
