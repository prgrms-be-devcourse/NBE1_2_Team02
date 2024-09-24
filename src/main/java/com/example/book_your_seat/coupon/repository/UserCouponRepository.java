package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
