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


    /**
     * 약식 방법으로 네임드락을 사용하는 방법
     * @param userId
     * @param couponId
     * @return
     */
    public CouponResponse issueCoupon(Long userId, Long couponId) {
        try {
            lockRepository.getLock(couponId.toString());
            couponCommandService.issueCoupon(userId, couponId);
        } finally {
            lockRepository.releaseLock(couponId.toString());
        }
        return new CouponResponse(couponId);
    }

    /**
     * JDBC를 활용해서 네임드 락을 구현하는 방법
     * @param userId
     * @param couponId
     * @return
     */
    public CouponResponse issueCouponWithNamedLock(Long userId, Long couponId) {
        return namedLockTemplate.executeWithLock(
                "userLockName",
                3000,
                () -> couponCommandService.issueCoupon(userId, couponId)
        );
    }

}
