package com.example.book_your_seat.user.service.query;

import com.example.book_your_seat.user.controller.dto.AddressResponse;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.coupon.CouponConst.USER_NOT_FOUND;
import static com.example.book_your_seat.user.UserConst.ALREADY_JOIN_EMAIL;

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

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddressList(Long userId) {
        User user = getUserByUserId(userId);
        return user.getAddressList().stream()
                .map(address -> new AddressResponse(
                        address.getPostcode(),
                        address.getDetail()
                )).toList();
    }

    @Override
    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ALREADY_JOIN_EMAIL);
        }
    }

}
