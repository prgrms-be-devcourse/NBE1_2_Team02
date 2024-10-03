package com.example.book_your_seat.seat.service.facade.impl;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import com.example.book_your_seat.seat.service.facade.SeatService;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatCommandService seatCommandService;
    private final SeatQueryService seatQueryService;

    @Override
    public SelectSeatResponse selectSeat(SelectSeatRequest request) {
        List<Seat> seats = seatCommandService.selectSeat(request);

        seatCommandService.cacheSeatIds(seats);

        return SelectSeatResponse.fromSeats(seats);
    }

    @Override
    @SeatLock
    public SelectSeatResponse selectSeatRedisson(SelectSeatRequest request) {
        List<Seat> seats = seatCommandService.selectSeatRedisson(request);

        seatCommandService.cacheSeatIds(seats);

        return SelectSeatResponse.fromSeats(seats);
    }

    @Override
    public List<RemainSeatResponse> findRemainSeats(Long concertId) {
        return seatQueryService.findRemainSeats(concertId);
    }
}
