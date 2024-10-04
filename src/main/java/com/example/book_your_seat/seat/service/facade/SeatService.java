package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.SeatCommandService;
import com.example.book_your_seat.seat.service.SeatQueryService;
import com.example.book_your_seat.seat.service.dto.SelectSeatsCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatCommandService seatCommandService;
    private final SeatQueryService seatQueryService;

    public List<Seat> findAllSeats(Long concertId) {
        return seatQueryService.findAllSeats(concertId);
    }

    public void selectSeats(SelectSeatsCommand command) {
        seatCommandService.selectSeats(command);
    }
}
