package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.payment.controller.dto.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.FinalPriceResponse;
import com.example.book_your_seat.payment.controller.dto.PaymentDto;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;
import com.example.book_your_seat.payment.domain.Payment;

import java.util.concurrent.CompletableFuture;

public interface PaymentService {
    FinalPriceResponse getFinalPrice(final FinalPriceRequest request);

    CompletableFuture<TossPaymentConfirmSuccessResponse> requestConfirm(final TossPaymentConfirmRequest request);

    Payment createPayment(final PaymentDto paymentDto);
}
