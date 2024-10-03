package com.example.book_your_seat.seat.service.query.impl;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.seat.SeatConst.SEAT_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Component
public class SeatQueryServiceImpl implements SeatQueryService {
    private final SeatRepository seatRepository;


    @Override
    public List<RemainSeatResponse> findRemainSeats(final Long concertId) {
        return seatRepository.findByConcertIdAndNotSold(concertId).stream()
                .map(RemainSeatResponse::from)
                .toList();
    }

    @Override
    public Integer getSeatPrice(final Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(
                () -> new IllegalArgumentException(SEAT_NOT_FOUND)
        );

        return seat.getConcert().getPrice();
    }


}
