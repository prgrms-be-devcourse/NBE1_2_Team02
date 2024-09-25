package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.coupon.CouponConst.COUPON_OUT_OF_STOCK;
import static com.example.book_your_seat.coupon.CouponConst.DUPLICATE_BAD_REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCommandServiceImpl implements CouponCommandService {

    private final UserCouponRepository userCouponRepository;
    private final CouponQueryService couponQueryService;

    //비관적 락
    public UserCouponResponse issueCouponWithPessimistic(User user, Long couponId) {

        Coupon coupon = couponQueryService.findByIdWithPessimistic(couponId);

        //선착순 쿠폰 중복수령 방지
        checkDuplicate(user.getId(), couponId);

        //수량 체크
        if(coupon.getAmount() <= 0) {
            throw new IllegalArgumentException(COUPON_OUT_OF_STOCK);
        }

        coupon.decreaseAmount();

        return new UserCouponResponse(
                userCouponRepository.save(new UserCoupon(user, coupon)).getId()
        );
    }

    //낙관적 락
    public UserCouponResponse issueCouponWithOptimistic(User user, Long couponId) {

        Coupon coupon = couponQueryService.findByIdWithOptimistic(couponId);

        //선착순 쿠폰 중복수령 방지
        checkDuplicate(user.getId(), couponId);

        //수량 체크
        if(coupon.getAmount() <= 0) {
            throw new IllegalArgumentException(COUPON_OUT_OF_STOCK);
        }

        coupon.decreaseAmount();

        return new UserCouponResponse(
                userCouponRepository.save(new UserCoupon(user, coupon)).getId()
        );

    }

    private void checkDuplicate(Long userId, Long couponId) {
        userCouponRepository.findByUserIdAndCouponId(userId, couponId).ifPresent(userCoupon -> {
                    throw new IllegalArgumentException(DUPLICATE_BAD_REQUEST);
                }
        );
    }
}
