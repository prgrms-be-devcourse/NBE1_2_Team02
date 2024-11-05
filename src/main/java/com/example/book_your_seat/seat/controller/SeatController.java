package com.example.book_your_seat.seat.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.service.facade.SeatFacade;
import com.example.book_your_seat.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seats/{concertId}")
public class SeatController {

    private final SeatFacade seatFacade;

    @Operation(
            summary = "잔여 좌석을 조회합니다.",
            description = "특정 콘서트의 잔여 좌석을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<SeatResponse> findAllSeats(
            @PathVariable final Long concertId
    ) {
        SeatResponse response = seatFacade.findAllSeats(concertId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "특정 좌석을 선택합니다.",
            description = "특정 좌석을 선택합니다."
    )
    @PostMapping
    public ResponseEntity<SeatResponse> selectSeat(
            @LoginUser User user,
            @PathVariable final Long concertId,
            @RequestBody @Valid final SelectSeatRequest request
    ) {
        SeatResponse response = seatFacade.selectSeat(request, user.getId());
        return ResponseEntity.ok(response);
    }

}
