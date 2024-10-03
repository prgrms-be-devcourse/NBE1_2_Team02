package com.example.book_your_seat.reservation.controller;

import com.example.book_your_seat.reservation.controller.dto.ConfirmationReservationRequest;
import com.example.book_your_seat.reservation.controller.dto.ConfirmationReservationResponse;
import com.example.book_your_seat.reservation.service.facade.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/Confirmation")
    public ResponseEntity<ConfirmationReservationResponse> ConfirmationReservation(
            @Valid @RequestBody final ConfirmationReservationRequest request) {
        CompletableFuture<ConfirmationReservationResponse> response
                = reservationService.confirmationReservation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response.join());

    }
}
