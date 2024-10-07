package com.example.book_your_seat.aop.seatLock;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
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
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.seat.SeatConst.REDISSON_LOCK_KEY;

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
                .map(seatId -> REDISSON_LOCK_KEY + seatId)
                .sorted()
                .toArray(String[]::new);

        RLock[] locks = Arrays.stream(lockKeys)
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        RedissonMultiLock multiLock = new RedissonMultiLock(locks);

        return tryLockAndProceed(joinPoint, multiLock);
    }

    private Object tryLockAndProceed(final ProceedingJoinPoint joinPoint, final RedissonMultiLock multiLock) throws Throwable {
        boolean acquired = false;
        try {
            acquired = multiLock.tryLock(2, 1, TimeUnit.SECONDS);
            if (!acquired) {
                throw new IllegalArgumentException("락을 획득할 수 없습니다.");
            }

            log.info("락 획득 성공");
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드에 인터럽트 상태를 설정
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (acquired && multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
            }
        }
    }
}