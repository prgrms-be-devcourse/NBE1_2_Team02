package com.example.book_your_seat.seat.mager;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.seat.SeatConst;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeatManager {
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public List<Seat> selectSeat(final SelectSeatRequest request) {
        List<Seat> seats = seatRepository.findAllByIdWithLock(request.seatIds());

        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });
        return seatRepository.saveAll(seats);
    }

    @Transactional
    public void cacheSeatIds(final List<Seat> seats) {
        seats.forEach(seat -> {
            String redisKey = "seat:" + seat.getId();
            try {
                redisTemplate.opsForValue().set(redisKey, seat.getId(), 30, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new IllegalArgumentException(SEAT_SOLD,e);
            }
        });
    }
    @Transactional
    public List<Seat> selectSeatRedisson(final SelectSeatRequest request) {
        List<Seat> seats = seatRepository.findAllById(request.seatIds());

        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });
        return seatRepository.saveAll(seats);
    }
}

