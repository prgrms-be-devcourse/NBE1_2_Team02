package com.example.book_your_seat.seat.service.command.Impl;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Service
@Transactional
@RequiredArgsConstructor
@Qualifier("Radisson")
public class SeatCommandServiceRadissonLock implements SeatCommandService {
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public SelectSeatResponse selectSeat(final SelectSeatRequest request) {
        //seat를 가져옴
        List<Seat> seats = seatRepository.findAllByIdWithLock(request.seatIds());

        validateAndSelectSeats(seats);

        cacheSeatIds(seats);

        return SelectSeatResponse.fromSeats(seats);
    }

    //좌석 isSold = true로 변환
    @SeatLock
    private void validateAndSelectSeats (final List<Seat> seats) {
        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SEAT_SOLD);
            }
            seat.selectSeat();
        });

        seatRepository.saveAll(seats);
    }

    //redis에 캐싱 TTL 설정
    private void cacheSeatIds(final List<Seat> seats) {
        seats.forEach(seat -> {
            String redisKey = "seat:" + seat.getId();
            try {
                redisTemplate.opsForValue().set(redisKey, seat.getId(), 30, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new IllegalArgumentException(SEAT_SOLD,e);
            }
        });
    }
}
