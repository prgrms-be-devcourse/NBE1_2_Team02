package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.UserCoupon;

import java.util.List;

public interface CouponQueryService {

    List<CouponDetailResponse> getCouponDetail(Long userId);
    CouponDetailResponse getCouponDetailById(Long userCouponId);

}
