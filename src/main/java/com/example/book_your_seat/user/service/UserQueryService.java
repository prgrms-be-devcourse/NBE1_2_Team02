package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;

public interface UserQueryService {
    User getUser(Long userId);
    Address getAddress(Long addressId);
}
