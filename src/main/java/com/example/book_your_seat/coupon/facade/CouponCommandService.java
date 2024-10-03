package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.domain.UserCoupon;

public interface CouponCommandService {

    CouponResponse createCoupon(CouponCreateRequest couponCreateRequest);
    UserCouponIdResponse issueCouponWithPessimistic(Long userId, Long couponId);
    UserCoupon userCoupon(Long userCouponId);

}
