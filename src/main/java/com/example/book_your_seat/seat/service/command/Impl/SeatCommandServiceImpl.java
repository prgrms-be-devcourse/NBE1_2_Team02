package com.example.book_your_seat.seat.service.command.Impl;

import com.example.book_your_seat.seat.SeatConst;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Component
@RequiredArgsConstructor
@Transactional
@Primary
@Qualifier("Pessimistic")
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatRepository seatRepository;
    private final RedisTemplate<String, Object> redisTemplate;

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
