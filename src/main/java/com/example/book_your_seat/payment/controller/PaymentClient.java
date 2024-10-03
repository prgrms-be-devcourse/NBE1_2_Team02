package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "paymentClient", url = "http://your-payment-service-url")
public interface PaymentClient {
    @PostMapping("/payments/confirm")
    CompletableFuture<TossPaymentConfirmSuccessResponse> confirmPayment(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody final TossPaymentConfirmRequest request
    );
}
