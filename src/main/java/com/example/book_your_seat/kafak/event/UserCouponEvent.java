package com.example.book_your_seat.kafak.event;

public record UserCouponEvent (
        Long userId,
        Long couponId
) {
}
