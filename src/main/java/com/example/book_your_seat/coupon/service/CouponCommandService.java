package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.Dto.CouponRequest;
import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.Dto.UserCouponResponse;

public interface CouponCommandService {

    UserCouponResponse useCoupon(Long userId, Long couponId);



    CouponResponse saveCoupon(CouponRequest couponRequest);



}
