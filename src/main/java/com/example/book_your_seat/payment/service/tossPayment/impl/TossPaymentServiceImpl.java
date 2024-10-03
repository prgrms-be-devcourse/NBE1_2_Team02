package com.example.book_your_seat.payment.service.tossPayment.impl;

import com.example.book_your_seat.payment.controller.PaymentClient;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmErrorResponse;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;
import com.example.book_your_seat.payment.service.tossPayment.TossPaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements TossPaymentService {
    private static final String SECRET_KEY = "yourSecretKey";
    private final PaymentClient paymentClient;

    @Override
    @Async
    public CompletableFuture<TossPaymentConfirmSuccessResponse> requestConfirm(final TossPaymentConfirmRequest request) {
        String auth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());

        try {
            return paymentClient.confirmPayment("Basic " + auth, request);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                TossPaymentConfirmErrorResponse errorResponse = objectMapper
                        .readValue(exception.getResponseBodyAsString(),
                                TossPaymentConfirmErrorResponse.class);
                throw new IllegalArgumentException(errorResponse.message());
            }catch (JsonProcessingException jsonEx) {
                throw new IllegalStateException("Failed to parse error response", jsonEx);
            }
        }
    }
}
