package com.example.book_your_seat.user.service.query;

import com.example.book_your_seat.user.domain.User;

public interface UserQueryService {

    User getUserByUserId(Long userId);
    User getUserWithUserCoupons(Long userId);
}
