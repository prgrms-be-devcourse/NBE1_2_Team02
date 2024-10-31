package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.service.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponFacade couponFacade;

    @PostMapping
    public ResponseEntity<CouponResponse> addCoupon(
            @RequestBody CouponCreateRequest couponCreateRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(couponFacade.createCoupon(couponCreateRequest));
    }
}
