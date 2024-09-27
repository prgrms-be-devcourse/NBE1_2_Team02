package com.example.book_your_seat.reservation.controller.dto;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public record AddReservationRequest(

        Long userId,
        Long concertId,
        Long userCouponId,
        List<Seat> seats

) {

    public static Reservation to(AddReservationRequest request) {
        return new Reservation(
                ReservationStatus.ORDERED,
                request.userCouponId(),
                request.concertId);
    }

}
