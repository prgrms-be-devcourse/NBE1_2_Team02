package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.manager.CouponManager;
import com.example.book_your_seat.coupon.manager.UserCouponManager;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.coupon.CouponConst.COUPON_ALREADY_USED;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCommandFacade implements CouponCommandService {

    private final UserManager userManager;
    private final CouponManager couponManager;
    private final UserCouponManager userCouponManager;

    @Override
    public CouponResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon coupon = couponCreateRequest.toCoupon();
        Coupon savedCoupon = couponManager.save(coupon);
        return new CouponResponse(savedCoupon.getId());
    }

    @Override
    public UserCouponIdResponse issueCouponWithPessimistic(Long userId, Long couponId) {
        User user = userManager.getUser(userId);
        Coupon coupon = couponManager.findByIdWithPessimistic(couponId);

        userCouponManager.checkAlreadyIssuedUser(userId, couponId);

        coupon.issue();
        couponManager.saveAndFlush(coupon);
        return new UserCouponIdResponse(
                userCouponManager.save(new UserCoupon(user, coupon)).getId()
        );
    }

    @Override
    public UserCoupon userCoupon(Long userCouponId) {
        UserCoupon userCoupon = userCouponManager.getUserCoupon(userCouponId);

        if (userCoupon.isUsed()) {
            throw new IllegalArgumentException(COUPON_ALREADY_USED);
        }

        userCoupon.useCoupon();

        return userCouponManager.save(userCoupon);

    }

}
