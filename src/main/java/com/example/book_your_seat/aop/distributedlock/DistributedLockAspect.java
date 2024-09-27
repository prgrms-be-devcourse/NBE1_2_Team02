package com.example.book_your_seat.aop.distributedlock;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class DistributedLockAspect {

    private final RedissonClient redisson;

    public DistributedLockAspect(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Around("@annotation(com.example.book_your_seat.aop.distributedlock.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        Long couponId = (Long) joinPoint.getArgs()[1];
        String lockKey = distributedLock.key();

        RLock lock = redisson.getLock(lockKey + couponId);
        try {
            boolean acquired = lock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );
            log.info("get lock");
            if (!acquired) {
                throw new IllegalArgumentException();
            }
            return joinPoint.proceed();
        } finally {
            lock.unlock();
            log.info("lock released");
        }
    }
}
