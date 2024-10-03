package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
    Optional<UserCoupon> findByIdAndUsed(Long userCouponId, Boolean isUsed);

}
