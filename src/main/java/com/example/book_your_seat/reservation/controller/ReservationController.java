package com.example.book_your_seat.reservation.controller;

import com.example.book_your_seat.reservation.controller.dto.AddReservationRequest;
import com.example.book_your_seat.reservation.controller.dto.ReservationResponse;
import com.example.book_your_seat.reservation.controller.dto.SimpleReservationResponse;
import com.example.book_your_seat.reservation.service.ReservationCommandService;
import com.example.book_your_seat.reservation.service.ReservationQueryService;
import com.example.book_your_seat.reservation.service.command.ReservationsCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    // 근데 이러면 관리자가 조회하는 '진짜' 모든 reservations 는 이름을 어떻게 짓지
    @GetMapping("/{userId}")
    public ResponseEntity<List<SimpleReservationResponse>> findAllReservations(
            @PathVariable Long userId,
            @RequestParam int pageSize,
            @RequestParam Long lastReservationId) {

        ReservationsCommand command = ReservationsCommand
                .from(userId, pageSize, lastReservationId);

        List<SimpleReservationResponse> responses
                = reservationQueryService.findAllReservations(command);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{userId}/{reservationId}")
    public ResponseEntity<ReservationResponse> findReservation(
            @PathVariable Long userId,
            @PathVariable Long reservationId) {

        ReservationResponse response
                = reservationQueryService.findReservationById(userId, reservationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> bookReservation(AddReservationRequest request) {
        ReservationResponse response
                = reservationCommandService.bookReservation(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationCommandService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
