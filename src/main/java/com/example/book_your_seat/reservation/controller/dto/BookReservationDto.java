package com.example.book_your_seat.reservation.controller.dto;

import com.example.book_your_seat.payment.domain.Payment;

import java.util.List;

public record BookReservationDto(
        List<Long> seatIds,
        Long addressId,
        Long userId,
        Long couponId,
        Payment payment
) {
    public static BookReservationDto from(List<Long> seatIds, Long addressId, Long userId,Long couponId, Payment payment) {
        return new BookReservationDto(seatIds, addressId, userId, couponId, payment);
    }
}
