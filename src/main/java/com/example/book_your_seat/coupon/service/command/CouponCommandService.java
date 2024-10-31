package com.example.book_your_seat.coupon.service.command;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.service.query.UserCouponQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCommandService {

    private final UserCouponQueryService userCouponQueryService;
    private final CouponRepository couponRepository;

    public CouponResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon coupon = couponCreateRequest.toCoupon();
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponResponse(savedCoupon.getId());
    }

    public void useUserCoupon(Long userCouponId) {
        UserCoupon validUserCoupon = userCouponQueryService.findValidUserCoupon(userCouponId);
        userCouponQueryService.updateUserCoupon(validUserCoupon);
    }

    public void saveAndFlush(Coupon coupon) {
        couponRepository.saveAndFlush(coupon);
    }
}
