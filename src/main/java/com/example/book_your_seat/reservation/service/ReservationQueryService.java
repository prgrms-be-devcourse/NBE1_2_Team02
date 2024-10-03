package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.reservation.domain.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReservationQueryService {

    Reservation findReservationById(UUID id);

    Reservation findReservationBySeatsId(List<Long> seatsId);
}
