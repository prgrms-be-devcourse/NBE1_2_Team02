package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;
import static com.example.book_your_seat.common.constants.Constants.NOT_VALIDATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usersCoupon")
public class UserCouponController {
    private final CouponCommandService couponCommandService;

    @PostMapping()
    public ResponseEntity<CouponResponse> getCoupon(@RequestParam Long couponId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        Long userId = sessionMember(session);

        CouponResponse couponResponse = couponCommandService.useCoupon(userId, couponId);

        return new ResponseEntity<>(couponResponse, HttpStatus.OK);

    }

    private static Long sessionMember(HttpSession session) {
        if(session == null) {
            throw new IllegalArgumentException(NOT_VALIDATION);
        }
        UserResponse userResponse = (UserResponse) session.getAttribute(LOGIN_USER);

        return userResponse.userId();
    }
}
