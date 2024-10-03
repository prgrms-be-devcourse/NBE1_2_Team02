package com.example.book_your_seat.coupon.manager;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.coupon.CouponConst.ALREADY_ISSUED_USER;
import static com.example.book_your_seat.coupon.CouponConst.COUPON_NOT_FOUND;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCouponManager {

    private final UserCouponRepository userCouponRepository;

    public void checkAlreadyIssuedUser(Long userId, Long couponId) {
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalArgumentException(ALREADY_ISSUED_USER);
        }
    }

    @Transactional
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponRepository.save(userCoupon);
    }

    public UserCoupon getUserCoupon(Long couponId) {
        return userCouponRepository.findById(couponId).orElseThrow(
                () -> new IllegalArgumentException(COUPON_NOT_FOUND)
        );
    }
}
