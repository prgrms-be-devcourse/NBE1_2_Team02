package com.example.book_your_seat.coupon.service;

import static com.example.book_your_seat.coupon.CouponConst.USER_NOT_FOUND;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryServiceImpl implements CouponQueryService {

    private final UserRepository userRepository;

    @Override
    public List<CouponDetailResponse> getCouponDetail(Long userId) {
        User user = getUser(userId);
        return user.getUserCoupons().stream()
                .map(UserCoupon::getCoupon)
                .map(coupon -> new CouponDetailResponse(coupon.getDiscountRate().getStringForm(), coupon.getCreatedAt().toLocalDate()))
                .toList();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }

}
