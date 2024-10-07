package com.example.book_your_seat.coupon.manager;

import static com.example.book_your_seat.coupon.CouponConst.COUPON_NOT_FOUND;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponManager {

    private final CouponRepository couponRepository;

    @Transactional
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon findByIdWithPessimistic(Long couponId) {
        return couponRepository.findByIdWithPessimistic(couponId)
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_FOUND));
    }

    @Transactional
    public void saveAndFlush(Coupon coupon) {
        couponRepository.saveAndFlush(coupon);
    }
}
