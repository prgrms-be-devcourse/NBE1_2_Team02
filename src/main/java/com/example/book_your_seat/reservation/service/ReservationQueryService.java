package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.reservation.controller.dto.ReservationResponse;
import com.example.book_your_seat.reservation.controller.dto.SimpleReservationResponse;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.command.ReservationsCommand;

import java.util.List;

public interface ReservationQueryService {

    List<SimpleReservationResponse> findAllReservations(ReservationsCommand command);
    ReservationResponse findReservationById(Long userId, Long reservationId);
}
