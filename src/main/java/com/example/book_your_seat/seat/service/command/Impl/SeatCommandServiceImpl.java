package com.example.book_your_seat.seat.service.command.Impl;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.mager.SeatManager;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Service
@RequiredArgsConstructor
@Transactional
@Primary
@Qualifier("Pessimistic")
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatManager seatManager;

    @Override
    public SelectSeatResponse selectSeat(final SelectSeatRequest request) {
        List<Seat> seats = seatManager.selectSeat(request);

        seatManager.cacheSeatIds(seats);

        return SelectSeatResponse.fromSeats(seats);
    }
}
