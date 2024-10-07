package com.example.book_your_seat.user.service.facade;

import com.example.book_your_seat.user.controller.dto.*;


public interface UserFacade {
    AddressIdResponse addAddress(Long userId, AddAddressRequest addAddressRequest);
    AddressIdResponse deleteAddress(Long userId, Long addressId);
}
