package com.example.book_your_seat.aop.seatLock;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SeatLockAspect {
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.example.book_your_seat.aop.seatLock.SeatLock) && args(request)")
    public Object lock(final ProceedingJoinPoint joinPoint, final SelectSeatRequest request) throws Throwable {
        // 좌석 ID를 정렬하여 락 키를 생성
        String[] lockKeys = request.seatIds().stream()
                .map(seat -> "seat-lock" + seat)
                .sorted()
                .toArray(String[]::new);

        RLock[] locks = Arrays.stream(lockKeys)
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        RedissonMultiLock multiLock = new RedissonMultiLock(locks);

        boolean acquired = false;
        try {
            acquired = multiLock.tryLock(2, 1, TimeUnit.SECONDS);

            if (!acquired) {
                throw new IllegalArgumentException("락 불가 !!!!!!!!!!!!!!!!!!!!!");
            }
            log.info("락!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
