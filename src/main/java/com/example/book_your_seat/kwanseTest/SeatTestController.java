package com.example.book_your_seat.kwanseTest;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class SeatTestController {

    private final SeatCommandService seatCommandService;


    /**
     * 예약할 좌석만 받으면 예약을 시켜버려
     * @param selectSeatRequest -> List<Long> seatsId
     * @return
     */
    @PostMapping("/seat/read")
    public ResponseEntity<List<Integer>> readSelectSeat(
            @Valid @RequestBody final SelectSeatRequest selectSeatRequest
    ) {
        List<Seat> seats = seatCommandService.selectSeat(selectSeatRequest);
        List<Integer> requestedId = seats.stream()
                .map(Seat::getSeatNumber)
                .toList();
        return ResponseEntity.ok(requestedId);
    }

    @PostMapping("/seat/write")
    public ResponseEntity<List<Integer>> writeSelectSeat(
            @Valid @RequestBody final SelectSeatRequest selectSeatRequest
    ) {
        List<Seat> seats = seatCommandService.selectSeatWrite(selectSeatRequest);
        List<Integer> requestedId = seats.stream()
                .map(Seat::getSeatNumber)
                .toList();
        return ResponseEntity.ok(requestedId);
    }

    @PostMapping("/seat/refactor")
    public ResponseEntity<Integer> refactorSelectSeat(
            @Valid @RequestBody final SelectSeatRequest selectSeatRequest
    ) {
        int affectedRows = seatCommandService.selectSeatRefactor(selectSeatRequest);
        return ResponseEntity.ok(affectedRows);
    }
}
