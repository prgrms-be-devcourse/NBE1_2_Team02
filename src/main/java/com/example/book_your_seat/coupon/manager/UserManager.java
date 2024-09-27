package com.example.book_your_seat.coupon.manager;

import static com.example.book_your_seat.coupon.CouponConst.USER_NOT_FOUND;

import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }

    public User getUserWithUserCoupons(Long userId) {
        return userRepository.findByIdWithUserCoupons(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }

}
