package com.example.book_your_seat.reservation.service.facade;

import com.example.book_your_seat.reservation.controller.dto.ConfirmationReservationRequest;
import com.example.book_your_seat.reservation.controller.dto.ConfirmationReservationResponse;

import java.util.concurrent.CompletableFuture;

public interface ReservationService {
    CompletableFuture<ConfirmationReservationResponse> confirmationReservation(final ConfirmationReservationRequest request);

}
