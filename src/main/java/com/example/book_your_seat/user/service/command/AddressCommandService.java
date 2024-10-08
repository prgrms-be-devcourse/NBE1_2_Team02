package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressIdResponse;
import com.example.book_your_seat.user.domain.User;

public interface AddressCommandService {

    AddressIdResponse addAddress(User user, AddAddressRequest addAddressRequest);

    AddressIdResponse deleteAddress(Long addressId);

}
