package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressResponse;
import com.example.book_your_seat.user.domain.User;

public interface AddressCommandService {
    AddressResponse addAddress(User user, AddAddressRequest addAddressRequest);
    AddressResponse deleteAddress(Long addressId);

}
