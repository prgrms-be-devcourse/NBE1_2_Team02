package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@RestController
public class CouponController {

    private final CouponCommandService couponCommandService;

    @PostMapping()
    public ResponseEntity<UserCouponResponse> issueCouponWithPessimistic(
            @SessionAttribute(LOGIN_USER) User user, @RequestParam("couponId") Long couponId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(couponCommandService.issueCouponWithPessimistic(user, couponId));
    }
}
