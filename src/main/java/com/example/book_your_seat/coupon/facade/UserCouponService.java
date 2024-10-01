package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserCouponService {

    Slice<UserCouponResponse> getUserCoupons(UserCouponRequest userCouponRequest, Long userId, Pageable pageable);
}
