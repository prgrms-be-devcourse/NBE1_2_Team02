package com.example.book_your_seat.user.service.query;

import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.coupon.CouponConst.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }

    @Override
    public User getUserWithUserCoupons(Long userId) {
        return userRepository.findByIdWithUserCoupons(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }
}
