package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.config.OpenFeignConfig;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmCommand;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "TossOpenFeign",
        url = "https://api.tosspayments.com",
        configuration = OpenFeignConfig.class
)
public interface TossOpenFeign {

    @PostMapping("/payments/confirm")
    TossConfirmResponse confirm(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody final TossConfirmCommand command);
}