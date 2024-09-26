package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    @Query("select count(us) from UserCoupon us")
    long countAll();

}
