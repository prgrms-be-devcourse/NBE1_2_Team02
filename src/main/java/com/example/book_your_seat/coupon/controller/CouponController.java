package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponIdResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.coupon.service.CouponQueryService;
import com.example.book_your_seat.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@RestController
public class CouponController {

    private final CouponCommandService couponCommandService;
    private final CouponQueryService couponQueryService;

    /**
     * 쿠폰 생성 (추후 관리자 권한 추가)
     * @param couponCreateRequest{amount, discountRate}
     * @return couponResponse{couponId}
     */
    @PostMapping
    public ResponseEntity<CouponIdResponse> createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponCommandService.createCoupon(couponCreateRequest));
    }

    /**
     * 내 쿠폰 목록 조회
     * @param user
     * @return List<UserCouponResponse{userCouponId, discountRate, expirationDate, isUsed}>
     */
    @GetMapping("/my")
    public ResponseEntity<List<UserCouponResponse>> getUserCoupons(@SessionAttribute(LOGIN_USER) User user) {
        return ResponseEntity.ok().body(couponQueryService.getUserCoupons(user));
    }

}
