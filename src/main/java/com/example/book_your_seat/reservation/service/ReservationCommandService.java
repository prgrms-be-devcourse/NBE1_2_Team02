package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.reservation.domain.Reservation;

public interface ReservationCommandService {

    Reservation saveReservation(Reservation reservation);
}
