package com.example.book_your_seat.aop.seatLock;

import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class SeatLockAspect {
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.example.book_your_seat.aop.seatLock.SeatLock) && args(seats)")
    public Object lock(final ProceedingJoinPoint joinPoint, final List<Seat> seats) throws Throwable {
        // 좌석 ID를 정렬하여 락 키를 생성
        String[] lockKeys = seats.stream()
                .map(seat -> "seat-lock" + seat.getId())
                .sorted()
                .toArray(String[]::new);

        RLock[] locks = Arrays.stream(lockKeys)
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        RedissonMultiLock multiLock = new RedissonMultiLock(locks);

        boolean acquired = false;
        try {
            acquired = multiLock.tryLock(10, 5, TimeUnit.SECONDS);

            if (!acquired) {
                throw new IllegalArgumentException("좌석이 이미 선택되었습니다.");
            }

            return aopForTransaction.proceed(joinPoint);
        }catch (InterruptedException e) {
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (acquired && multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
            }
        }

    }
}
