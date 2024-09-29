package com.example.book_your_seat.reservation.service.command;

import com.example.book_your_seat.reservation.controller.dto.BookReservationDto;
import com.example.book_your_seat.reservation.domain.Reservation;

public interface ReservationCommandService {
    Reservation bookReservation(final BookReservationDto dto);
}
