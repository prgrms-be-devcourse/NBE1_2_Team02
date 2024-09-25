package com.example.book_your_seat.coupon.controller;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.service.CouponCommandServiceImpl;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponCommandServiceImpl couponCommandService;

    @PostMapping
    public ResponseEntity<CouponResponse> addCoupon(
            @RequestBody CouponCreateRequest couponCreateRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(couponCommandService.createCoupon(couponCreateRequest));
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<CouponResponse> issueCoupone(
            @PathVariable("couponId") Long couponId,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponCommandService.issueCoupon(userId, couponId));
    }

    private Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserResponse userResponse = (UserResponse) session.getAttribute(LOGIN_USER);
        return userResponse.userId();
    }

}
