package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.facade.CouponCommandService;
import com.example.book_your_seat.coupon.facade.CouponQueryService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponCommandService couponCommandService;
    private final CouponQueryService couponQueryService;

    @PostMapping("/{couponId}")
    public ResponseEntity<UserCouponIdResponse> issueCoupon(
            @LoginUser User user,
            @PathVariable("couponId") Long couponId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponCommandService.issueCouponWithPessimistic(user.getId(), couponId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<CouponDetailResponse>> getCouponDetails(@LoginUser User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponQueryService.getCouponDetail(user.getId()));
    }

}
