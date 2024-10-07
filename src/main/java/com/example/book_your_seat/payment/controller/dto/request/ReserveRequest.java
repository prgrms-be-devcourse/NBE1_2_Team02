package com.example.book_your_seat.payment.controller.dto.request;

import java.util.List;

public record ReserveRequest(

        List<Long> seatsId,
        Long userCouponId,
        Long addressId
) {
}
