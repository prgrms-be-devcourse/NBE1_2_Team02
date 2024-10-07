package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.service.facade.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatResponse>> findAllSeats(
            @PathVariable final Long concertId
    ) {
        List<SeatResponse> responses = seatService.findAllSeats(concertId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/select")
    public ResponseEntity<SelectSeatResponse> selectSeat(@Valid @RequestBody final SelectSeatRequest selectSeatRequest) {
        SelectSeatResponse selectSeatResponse = seatService.selectSeat(selectSeatRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(selectSeatResponse);
    }

}
