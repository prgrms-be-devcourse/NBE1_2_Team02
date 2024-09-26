package com.example.book_your_seat.coupon.controller;

import com.example.book_your_seat.coupon.controller.Dto.CouponRequest;
import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponCommandService couponCommandService;


    @PostMapping
    public ResponseEntity<CouponResponse> addCoupon(@RequestBody CouponRequest couponRequest) {
        CouponResponse couponResponse = couponCommandService.saveCoupon(couponRequest);

        return new ResponseEntity<>(couponResponse, HttpStatus.CREATED);
    }



}
