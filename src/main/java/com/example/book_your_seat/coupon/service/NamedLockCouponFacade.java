package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.repository.LockRepository;
import com.example.book_your_seat.coupon.repository.NamedLockTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamedLockCouponFacade {

    private final LockRepository lockRepository;
    private final CouponCommandService couponCommandService;
    private final NamedLockTemplate namedLockTemplate;

    public CouponResponse issueCoupon(Long userId, Long couponId) {
        try {
            lockRepository.getLock(couponId.toString());
            couponCommandService.issueCoupon(userId, couponId);
        } finally {
            lockRepository.releaseLock(couponId.toString());
        }
        return new CouponResponse(couponId);
    }


    public CouponResponse issueCouponWithNamedLock(Long userId, Long couponId) {
        return namedLockTemplate.executeWithLock(
                "userLockName",
                3000,
                () -> couponCommandService.issueCoupon(userId, couponId)
        );
    }

}
