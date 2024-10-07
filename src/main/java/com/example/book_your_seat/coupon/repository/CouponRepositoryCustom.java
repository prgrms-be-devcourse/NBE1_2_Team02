package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CouponRepositoryCustom {
    Slice<UserCouponResponse> selectUserCoupons(UserCouponRequest userCouponRequest, Long memberId, Pageable pageable);
}
