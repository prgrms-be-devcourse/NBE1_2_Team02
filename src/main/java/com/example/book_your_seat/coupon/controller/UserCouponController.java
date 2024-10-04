package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.facade.UserCouponService;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/userCoupons")
public class UserCouponController {

    private final UserCouponService userCouponService;


    @GetMapping
    public Slice<UserCouponResponse> getUserCoupons( UserCouponRequest userCouponRequest, HttpServletRequest request, Pageable pageable) {
        Long userId = getUserId(request);
        return userCouponService.getUserCoupons(userCouponRequest, userId, pageable);
    }

    private Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserResponse userResponse = (UserResponse) session.getAttribute(LOGIN_USER);
        return userResponse.userId();
    }
}
