package com.example.book_your_seat.reservation.controller.dto;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;

public final class SimpleReservationResponse {

    private final Long reservationId;
    private final String concertTitle;
    private final ReservationStatus status;
    private final Integer finalPrice;

    private SimpleReservationResponse(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.concertTitle = reservation.getConcert().getTitle();
        this.status = reservation.getStatus();
        this.finalPrice = reservation.getFinalPrice();
    }

    public static SimpleReservationResponse from(Reservation reservation) {
        return new SimpleReservationResponse(reservation);
    }
}
