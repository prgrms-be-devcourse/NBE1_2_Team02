package com.example.book_your_seat.payment.service.tossPayment;

import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;

import java.util.concurrent.CompletableFuture;

public interface TossPaymentService {
    CompletableFuture<TossPaymentConfirmSuccessResponse> requestConfirm(final TossPaymentConfirmRequest request);
}
