package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.service.dto.SelectSeatsCommand;
import com.example.book_your_seat.seat.service.facade.SeatCommandService;
import com.example.book_your_seat.seat.service.facade.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seats/{concertId}")
public class SeatController {

    private final SeatCommandService seatCommandService;
    private final SeatQueryService seatQueryService;

    @GetMapping
    public ResponseEntity<List<SeatResponse>> findAllSeats(
            @PathVariable final Long concertId
    ) {
        List<SeatResponse> responses =
                seatQueryService.findAllSeats(concertId).stream()
                .map(SeatResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> selectSeats(
            @PathVariable final Long concertId,
            @PathVariable final Long userId,
            @RequestBody final SelectSeatRequest request
            ) {

        SelectSeatsCommand command = SelectSeatsCommand.from(concertId, userId, request);
        seatCommandService.selectSeats(command);
        return ResponseEntity.noContent().build();
    }
}
