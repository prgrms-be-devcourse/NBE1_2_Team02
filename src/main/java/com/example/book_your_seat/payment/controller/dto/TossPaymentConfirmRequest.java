package com.example.book_your_seat.payment.controller.dto;

public record TossPaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Integer amount
) {
        public static TossPaymentConfirmRequest from(final PaymentSuccessRequest request) {
                return new TossPaymentConfirmRequest(request.paymentKey(), request.orderId(), request.amount());
        }
}