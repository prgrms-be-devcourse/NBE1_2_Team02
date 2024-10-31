package com.example.book_your_seat.coupon.service.facade;

import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponFacade {

    private final CouponRepository couponRepository;

    public Slice<UserCouponResponse> getUserCoupons(UserCouponRequest userCouponRequest, Long userId, Pageable pageable) {
        return couponRepository.selectUserCoupons(userCouponRequest, userId, pageable);
    }
}
