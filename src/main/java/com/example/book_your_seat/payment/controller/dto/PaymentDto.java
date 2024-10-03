package com.example.book_your_seat.payment.controller.dto;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;

import java.time.LocalDateTime;

public record PaymentDto(
        Integer totalPrice,
        LocalDateTime executedAt,
        DiscountRate discountRate,
        String paymentKey,
        String orderId
) {
    public static PaymentDto from(TossPaymentConfirmSuccessResponse response, DiscountRate discountRate) {
        return new PaymentDto(response.totalAmount(), response.approvedAt(), discountRate, response.paymentKey(),response.orderId());
    }
}
