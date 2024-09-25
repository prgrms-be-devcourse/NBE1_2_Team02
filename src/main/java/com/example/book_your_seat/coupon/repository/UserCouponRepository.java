package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
