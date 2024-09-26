package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.user.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    @Query("SELECT uc FROM UserCoupon uc JOIN FETCH uc.coupon WHERE uc.user = :user")
    List<UserCoupon> findAllByUser(User user);
}
