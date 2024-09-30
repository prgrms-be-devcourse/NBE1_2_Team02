package com.example.book_your_seat.coupon.controller;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.facade.CouponCommandService;
import com.example.book_your_seat.coupon.facade.CouponQueryService;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponCommandService couponCommandService;
    private final CouponQueryService couponQueryService;

    @PostMapping
    public ResponseEntity<CouponResponse> addCoupon(
            @RequestBody CouponCreateRequest couponCreateRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(couponCommandService.createCoupon(couponCreateRequest));
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<UserCouponIdResponse> issueCoupon(
            @PathVariable("couponId") Long couponId,
            HttpServletRequest request
    ) {
//        Long userId = getUserId(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponCommandService.issueCouponWithPessimistic(1L, couponId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<CouponDetailResponse>> getCouponDetails(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(couponQueryService.getCouponDetail(userId));
    }

    private Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserResponse userResponse = (UserResponse) session.getAttribute(LOGIN_USER);
        return userResponse.userId();
    }

}
