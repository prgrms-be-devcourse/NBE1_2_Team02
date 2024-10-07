package com.example.book_your_seat.user.service.facade;

import com.example.book_your_seat.user.controller.dto.*;


public interface UserFacade {
    AddressResponse addAddress(Long userId, AddAddressRequest addAddressRequest);
    AddressResponse deleteAddress(Long userId, Long addressId);
}
