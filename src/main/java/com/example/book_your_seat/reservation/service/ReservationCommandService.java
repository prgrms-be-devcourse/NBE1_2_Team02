package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.reservation.controller.dto.AddReservationRequest;
import com.example.book_your_seat.reservation.controller.dto.ReservationResponse;

public interface ReservationCommandService {

    ReservationResponse bookReservation(AddReservationRequest request);
    void cancelReservation(Long reservationId);
}
