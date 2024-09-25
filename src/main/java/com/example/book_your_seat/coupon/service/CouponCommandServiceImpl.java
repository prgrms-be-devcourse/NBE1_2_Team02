package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.coupon.CouponConst.*;
import static com.example.book_your_seat.user.UserConst.NOTFOUND_USER;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CouponCommandServiceImpl implements CouponCommandService{

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CouponResponse useCoupon(Long userId, Long couponId) {
        validationUserCoupon(userId, couponId);

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NOTFOUND_USER));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException(NOTFOUND_COUPON)); //쿠폰 찾기

        userCouponRepository.save( new UserCoupon(user, coupon)); // 쿠폰 발급

        return CouponResponse.fromCoupon(coupon, COUPON_MESSAGE);
    }


    public void validationUserCoupon(Long userId, Long couponId) {
        if(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalArgumentException(VALIDATION_COUPON);
        }
    }

}
