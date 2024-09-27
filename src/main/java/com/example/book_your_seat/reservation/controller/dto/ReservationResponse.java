package com.example.book_your_seat.reservation.controller.dto;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public final class ReservationResponse {

    private final Long reservationId;
    private final Integer price;
    private final ReservationStatus status;
    private final Coupon coupon;
    private final List<Seat> seats;
    private final Concert concert;
    private final Long userId;

    private ReservationResponse(Reservation reservation, Coupon coupon) {
        this.reservationId = reservation.getId();
        this.price = reservation.getFinalPrice();
        this.status = reservation.getStatus();
        this.coupon = coupon;
        this.seats = reservation.getSeats();
        this.concert = reservation.getConcert();
        this.userId = reservation.getUser().getId();
    }

    public static ReservationResponse from(Reservation reservation, Coupon coupon) {
        return new ReservationResponse(reservation, coupon);
    }
}
