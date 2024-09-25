package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.user.domain.User;

public interface CouponCommandService {

    void useCoupon(User user, Long couponId);

}
