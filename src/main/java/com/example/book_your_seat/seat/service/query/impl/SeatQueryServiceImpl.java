package com.example.book_your_seat.seat.service.query.impl;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeatQueryServiceImpl implements SeatQueryService {
    private final SeatRepository seatRepository;


    @Override
    public List<RemainSeatResponse> findRemainSeats(final Long concertId) {
        return seatRepository.findByConcertIdAndNotSold(concertId).stream()
                .map(RemainSeatResponse::from)
                .toList();
    }

}
