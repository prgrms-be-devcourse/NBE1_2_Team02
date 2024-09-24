package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressResponse;
import com.example.book_your_seat.user.controller.dto.JoinRequest;
import com.example.book_your_seat.user.controller.dto.LoginRequest;
import com.example.book_your_seat.user.controller.dto.UserResponse;

public interface UserCommandService {

    UserResponse join(JoinRequest joinRequest);
    UserResponse login(LoginRequest loginRequest);
    AddressResponse addAddress(Long userId, AddAddressRequest addAddressRequest);
    AddressResponse deleteAddress(Long addressId);

}
