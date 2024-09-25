package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.coupon.CouponConst.INVALID_COUPON_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryServiceImpl implements CouponQueryService {
    private final CouponRepository couponRepository;

    @Override
    public Coupon findByIdWithPessimistic(Long couponId) {
        return couponRepository.findByIdWithPessimistic(couponId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_COUPON_ID + couponId));
    }

    @Override
    public Coupon findByIdWithOptimistic(Long couponId) {
        return couponRepository.findByIdWithOptimistic(couponId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_COUPON_ID + couponId));
    }

}
