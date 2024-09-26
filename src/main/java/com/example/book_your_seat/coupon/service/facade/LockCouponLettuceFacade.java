package com.example.book_your_seat.coupon.service.facade;

import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.repository.redis.RedisLockRepository;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockCouponLettuceFacade {

    private final CouponCommandService couponCommandService;
    private final RedisLockRepository redisLockRepository;
    private CouponResponse couponResponse;

    public CouponResponse useCoupon(Long userId, Long couponId) throws InterruptedException {

        while(!redisLockRepository.lock(couponId)) {

            Thread.sleep(100);
        }

        try{
            couponResponse = couponCommandService.useCoupon(userId, couponId);
        }finally {
            redisLockRepository.unlock(couponId);
        }

        return couponResponse;

    }
}
