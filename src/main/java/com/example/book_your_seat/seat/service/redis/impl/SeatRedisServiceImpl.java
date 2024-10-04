package com.example.book_your_seat.seat.service.redis.impl;


import com.example.book_your_seat.reservation.contorller.dto.ConfirmationReservationRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.redis.SeatRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.ACCEPTABLE_TIMEOUT;
import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Component
@RequiredArgsConstructor
public class SeatRedisServiceImpl implements SeatRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public void cacheSeatIds(List<Seat> seats, Long userId) {
        seats.forEach(seat -> {
            String redisKey = "seat:" + seat.getId();
            try {
                redisTemplate.opsForValue().set(redisKey, userId.toString(), 30, TimeUnit.MINUTES);
            }catch (Exception e){

                throw new IllegalArgumentException(SEAT_SOLD);
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public void validateSeat(ConfirmationReservationRequest request) {
        for (Long seatId : request.seatIds()) {
            String redisKey = "seat:" + seatId;

            if(!Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                throw new IllegalArgumentException(ACCEPTABLE_TIMEOUT);
            }
            // 저장된 userId와 요청된 userId 비교
            Long storedUserId = (Long) redisTemplate.opsForValue().get(redisKey);
            if (!request.userId().equals(storedUserId)) {
                throw new IllegalArgumentException(ACCEPTABLE_TIMEOUT);
            }
        }
    }

    @Override
    @Transactional
    public void deleteCache(final Long seatId) {
        // Redis에서 키 삭제
        String key = "seat:" + seatId;
        redisTemplate.delete(key);
    }


}
