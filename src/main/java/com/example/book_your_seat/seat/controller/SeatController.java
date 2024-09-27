package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/seat")
@RestController
public class SeatController {
    private final SeatQueryService seatQueryService;

    @GetMapping("/remainSeat/{concertId}")
    public ResponseEntity<List<RemainSeatResponse>> findRemainSeats(@PathVariable final Long concertId) {
        List<RemainSeatResponse> remainSeats = seatQueryService.findRemainSeats(concertId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(remainSeats);
    }
}
