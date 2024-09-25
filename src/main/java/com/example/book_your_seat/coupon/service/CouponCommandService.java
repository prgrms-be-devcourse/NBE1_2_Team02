package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.dto.UserCouponResponse;
import com.example.book_your_seat.user.domain.User;

public interface CouponCommandService {
    UserCouponResponse issueCouponWithPessimistic(User user, Long couponId);
    UserCouponResponse issueCouponWithOptimistic(User user, Long couponId);

}
