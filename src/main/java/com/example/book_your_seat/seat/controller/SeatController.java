package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.service.facade.SeatFacade;
import com.example.book_your_seat.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seats/{concertId}")
public class SeatController {

    private final SeatFacade seatFacade;

    @GetMapping
    public ResponseEntity<SeatResponse> findAllSeats(
            @PathVariable final Long concertId
    ) {
        SeatResponse response = seatFacade.findAllSeats(concertId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SeatResponse> selectSeat(
            @LoginUser User user,
            @PathVariable final Long concertId,
            @RequestBody @Valid final SelectSeatRequest request
    ) {
        SeatResponse response = seatFacade.selectSeat(user.getId(), concertId, request);
        return ResponseEntity.ok(response);
    }

}
