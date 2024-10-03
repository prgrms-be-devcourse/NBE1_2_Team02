package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.user.manager.UserManager;
import com.example.book_your_seat.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponQueryFacade implements CouponQueryService {

    private final UserManager userManager;

    @Override
    public List<CouponDetailResponse> getCouponDetail(Long userId) {
        User user = userManager.getUserWithUserCoupons(userId);
        return user.getUserCoupons().stream()
                .map(UserCoupon::getCoupon)
                .map(CouponDetailResponse::fromCoupon)
                .toList();
    }

}
