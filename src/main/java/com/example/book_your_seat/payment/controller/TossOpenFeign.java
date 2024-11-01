package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.request.TossCancelRequest;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.TossCancelResponse;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "TossOpenFeign",
        url = "https://api.tosspayments.com"
)
public interface TossOpenFeign {

    @PostMapping("/payments/confirm")
    TossConfirmResponse confirm(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody final TossConfirmRequest request);

    @PostMapping("/payment/{paymentKey}/cancel")
    TossCancelResponse cancel(
            @RequestHeader("Authorization") final String authorization,
            @PathVariable final String paymentKey,
            @RequestBody final TossCancelRequest request
    );
}
