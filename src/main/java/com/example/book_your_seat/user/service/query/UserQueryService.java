package com.example.book_your_seat.user.service.query;

import com.example.book_your_seat.user.controller.dto.AddressResponse;
import com.example.book_your_seat.user.domain.User;

import java.util.List;

public interface UserQueryService {

    User getUserByUserId(Long userId);

    User getUserWithUserCoupons(Long userId);

    List<AddressResponse> getUserAddressList(User user);

}
