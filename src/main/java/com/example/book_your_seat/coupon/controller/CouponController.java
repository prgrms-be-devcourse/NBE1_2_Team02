package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.service.facade.CouponFacade;
import com.example.book_your_seat.coupon.service.facade.UserCouponFacade;
import com.example.book_your_seat.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final UserCouponFacade userCouponFacade;
    private final CouponFacade couponFacade;

    @Operation(
            summary = "쿠폰을 발행합니다.",
            description = "쿠폰을 발행합니다."
    )
    @PostMapping("/{couponId}")
    public ResponseEntity<UserCouponIdResponse> issueCoupon(
            @LoginUser User user,
            @PathVariable("couponId") Long couponId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponFacade.issueCouponWithPessimistic(user.getId(), couponId));
    }

    @Operation(
            summary = "사용자 쿠폰을 조회합니다.",
            description = "사용자의 쿠폰을 조회합니다."
    )
    @GetMapping("/user")
    public Slice<UserCouponResponse> getUserCoupons(@LoginUser User user,
                                                    UserCouponRequest userCouponRequest,
                                                    Pageable pageable) {
        return userCouponFacade.getUserCoupons(userCouponRequest, user.getId(), pageable);
    }

}
