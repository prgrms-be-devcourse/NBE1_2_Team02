package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.facade.UserCouponService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/userCoupons")
public class UserCouponController {

    private final UserCouponService userCouponService;


    @GetMapping
    public Slice<UserCouponResponse> getUserCoupons(@LoginUser User user,
                                                    UserCouponRequest userCouponRequest,
                                                    Pageable pageable) {
        return userCouponService.getUserCoupons(userCouponRequest, user.getId(), pageable);
    }
}
