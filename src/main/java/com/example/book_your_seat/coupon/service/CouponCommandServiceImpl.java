package com.example.book_your_seat.coupon.service;

import static com.example.book_your_seat.coupon.CouponConst.ALREADY_ISSUED_USER;
import static com.example.book_your_seat.coupon.CouponConst.COUPON_NOT_FOUND;
import static com.example.book_your_seat.coupon.CouponConst.USER_NOT_FOUND;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCommandServiceImpl implements CouponCommandService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    @Override
    public CouponResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon coupon = couponCreateRequest.toCoupon();
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponResponse(savedCoupon.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CouponResponse issueCoupon(Long userId, Long couponId) {
//        checkAlreadyIssuedUser(userId, couponId);

        Coupon coupon = getCoupon(couponId);
        User user = getUser(userId);

        coupon.issue();
        userCouponRepository.save(new UserCoupon(user, coupon));

        return new CouponResponse(coupon.getId());
    }

    private void checkAlreadyIssuedUser(Long userId, Long couponId) {
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalArgumentException(ALREADY_ISSUED_USER);
        }
    }

    private Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }

}
