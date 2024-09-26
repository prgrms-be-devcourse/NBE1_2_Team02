package com.example.book_your_seat.coupon.service.facade;

import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.coupon.CouponConst.TIME_OUT;

@Service
@RequiredArgsConstructor
@Slf4j
public class LockCouponRedissonFacade {

    private final CouponCommandService couponCommandService;
    private final RedissonClient redisson;
    private  CouponResponse couponResponse;


    public CouponResponse useCoupon(Long userId, Long couponId) {
            RLock lock = redisson.getLock(String.format("purchase:book:%d", couponId)); // 쿠폰 아이디에 해당하는 분산락 생성

            try{
                boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS); //락을 10초 동안 기다리돼 락을 1초 동안 가질 수 있다 락을 획득하면 true 락을 얻지 못하면 false

                if(!available){
                    log.info(TIME_OUT);
                    throw new IllegalArgumentException();
                }
                couponResponse = couponCommandService.useCoupon(userId, couponId);
            }catch (InterruptedException e){
                throw new  RuntimeException(e);
            }finally {
                lock.unlock();
            }

            return couponResponse;
    }



}
