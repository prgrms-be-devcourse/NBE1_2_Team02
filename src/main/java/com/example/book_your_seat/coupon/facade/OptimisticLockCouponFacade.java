package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockCouponFacade {

    private final CouponCommandService couponCommandService;

    public void issueCouponWithOptimistic(User user, Long couponId) throws InterruptedException {
        while(true) {
            try {
                couponCommandService.issueCouponWithOptimistic(user, couponId);
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                Thread.sleep(50);
            }
        }
    }
}
