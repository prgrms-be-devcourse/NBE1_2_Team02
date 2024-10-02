package com.example.book_your_seat.seat.service.command.Impl;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.mager.SeatManager;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Service
@Transactional
@RequiredArgsConstructor
@Qualifier("Radisson")
public class SeatCommandServiceRadissonLock implements SeatCommandService {
    private final SeatManager seatManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @SeatLock
    public SelectSeatResponse selectSeat(final SelectSeatRequest request) {
        List<Seat> seats = seatManager.selectSeatRedisson(request);

        seatManager.cacheSeatIds(seats);

        return SelectSeatResponse.fromSeats(seats);
    }
}
