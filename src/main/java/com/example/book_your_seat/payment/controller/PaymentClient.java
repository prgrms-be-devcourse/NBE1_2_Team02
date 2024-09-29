package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.TossPaymentConfirmSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "paymentClient", url = "http://your-payment-service-url")
public interface PaymentClient {
    @PostMapping("/payments/confirm")
    TossPaymentConfirmSuccessResponse confirmPayment(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody final TossPaymentConfirmRequest request
    );
}
