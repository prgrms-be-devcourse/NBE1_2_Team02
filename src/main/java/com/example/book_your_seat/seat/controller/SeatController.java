package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/seat")
@RestController
public class SeatController {
    private final SeatQueryService seatQueryService;
    private final SeatCommandService seatCommandService;

    @GetMapping("/remainSeat/{concertId}")
    public ResponseEntity<List<RemainSeatResponse>> findRemainSeats(@PathVariable final Long concertId) {
        List<RemainSeatResponse> remainSeats = seatQueryService.findRemainSeats(concertId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(remainSeats);
    }

    @PostMapping("/selectSeat")
    public ResponseEntity<SelectSeatResponse> selectSeat(@Valid @RequestBody SelectSeatRequest selectSeatRequest) {
        SelectSeatResponse selectSeatResponse = seatCommandService.selectSeat(selectSeatRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(selectSeatResponse);
    }
}
