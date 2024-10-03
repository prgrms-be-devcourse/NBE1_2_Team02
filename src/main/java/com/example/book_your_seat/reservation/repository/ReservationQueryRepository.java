package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.dto.ReservationsCommand;

import java.util.List;

public interface ReservationQueryRepository {

    List<Reservation> findAllReservations(ReservationsCommand command);

    Reservation findReservationBySeatsId(List<Long> seatsId);
}
