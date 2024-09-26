package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponIdResponse;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@RestController
public class CouponController {

    private final CouponCommandService couponCommandService;

    /**
     * 쿠폰 생성 (추후 관리자 권한 추가)
     * @param couponCreateRequest{amount, discountRate}
     * @return couponResponse{couponId}
     */
    @PostMapping
    public ResponseEntity<CouponIdResponse> createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponCommandService.createCoupon(couponCreateRequest));
    }
}
