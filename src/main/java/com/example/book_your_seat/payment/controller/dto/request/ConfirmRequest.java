package com.example.book_your_seat.payment.controller.dto.request;

import java.util.List;

public record ConfirmRequest(

        String paymentKey,
        String orderId,
        Long amount,
        List<Long> seatsId,
        Long userCouponId
) {
}
