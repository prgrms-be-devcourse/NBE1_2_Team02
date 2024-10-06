package com.example.book_your_seat.coupon.manager;

import static com.example.book_your_seat.coupon.CouponConst.ALREADY_ISSUED_USER;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    public UserCoupon findValidUserCoupon(Long userCouponId) {
        return userCouponRepository.findByIdAndIsUsed(userCouponId, false)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_USER_COUPON"));
    }
    @Transactional
    public void updateUserCoupon(UserCoupon userCoupon) {
        userCoupon.use();
        userCouponRepository.save(userCoupon);
    }
}
