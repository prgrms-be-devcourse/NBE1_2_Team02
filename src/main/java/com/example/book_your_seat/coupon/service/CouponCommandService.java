package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.UserCoupon;

import java.lang.reflect.Member;

public interface CouponCommandService {

    UserCoupon useCoupon(Member member, Long couponId);

}
