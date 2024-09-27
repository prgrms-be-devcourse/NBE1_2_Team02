package com.example.book_your_seat.coupon.service;

import static com.example.book_your_seat.coupon.CouponConst.ALREADY_ISSUED_USER;
import static com.example.book_your_seat.coupon.CouponConst.USER_NOT_FOUND;

import com.example.book_your_seat.aop.distributedlock.DistributedLock;
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCommandServiceImpl implements CouponCommandService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponQueryService couponQueryService;

    @Override
    public CouponResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon coupon = couponCreateRequest.toCoupon();
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponResponse(savedCoupon.getId());
    }

    /*
    쿠폰 발급 - 비관적 락
    */
    @Override
    @DistributedLock(key = "couponId : ")
    public UserCouponIdResponse issueCouponWithPessimistic(Long userId, Long couponId) {

        User user = getUser(userId);
        Coupon coupon = couponQueryService.findByIdWithPessimistic(couponId);

        //선착순 쿠폰 중복수령 방지
//        checkAlreadyIssuedUser(userId, couponId);

        coupon.issue();
        couponRepository.saveAndFlush(coupon);
        return new UserCouponIdResponse(
                userCouponRepository.save(new UserCoupon(user, coupon)).getId()
        );
    }

    private void checkAlreadyIssuedUser(Long userId, Long couponId) {
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalArgumentException(ALREADY_ISSUED_USER);
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }



}
