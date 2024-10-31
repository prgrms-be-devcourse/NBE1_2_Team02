package com.example.book_your_seat.coupon.service.facade;

import com.example.book_your_seat.aop.distributedlock.DistributedLock;
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.service.command.CouponCommandService;
import com.example.book_your_seat.coupon.service.query.CouponQueryService;
import com.example.book_your_seat.coupon.service.query.UserCouponQueryService;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponCommandService couponCommandService;
    private final CouponQueryService couponQueryService;
    private final UserQueryService userQueryService;
    private final UserCouponQueryService userCouponQueryService;

    public CouponResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        return couponCommandService.createCoupon(couponCreateRequest);
    }

    @DistributedLock(key = "coupon_lock")
    public UserCouponIdResponse issueCouponWithPessimistic(Long userId, Long couponId) {
        User user = userQueryService.getUserByUserId(userId);
        Coupon coupon = couponQueryService.findByIdWithPessimistic(couponId);

        userCouponQueryService.checkAlreadyIssuedUser(userId, couponId);

        coupon.issue();
        couponCommandService.saveAndFlush(coupon);
        return new UserCouponIdResponse(
                userCouponQueryService.save(new UserCoupon(user, coupon)).getId()
        );
    }


}
