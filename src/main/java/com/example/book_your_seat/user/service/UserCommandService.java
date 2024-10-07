package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.controller.dto.*;

public interface UserCommandService {

    UserResponse join(JoinRequest joinRequest);
    TokenResponse login(LoginRequest loginRequest);
    AddressResponse addAddress(Long userId, AddAddressRequest addAddressRequest);
    AddressResponse deleteAddress(Long addressId);

}
