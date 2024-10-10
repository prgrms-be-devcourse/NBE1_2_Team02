package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.service.facade.SeatFacade;
import com.example.book_your_seat.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {

    private final SeatFacade seatFacade;

    @GetMapping("/{concertId}")
    public ResponseEntity<List<SeatResponse>> findAllSeats(
            @PathVariable final Long concertId
    ) {
        List<SeatResponse> responses = seatFacade.findAllSeats(concertId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/select")
    public ResponseEntity<SelectSeatResponse> selectSeat(
            @LoginUser User user,
            @Valid @RequestBody final SelectSeatRequest selectSeatRequest
    ) {
        SelectSeatResponse selectSeatResponse = seatFacade.selectSeat(selectSeatRequest, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(selectSeatResponse);
    }

}
