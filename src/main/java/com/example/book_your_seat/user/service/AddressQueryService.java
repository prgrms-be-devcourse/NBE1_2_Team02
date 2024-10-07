package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.domain.Address;

public interface AddressQueryService {

    Address getAddressByAddressId(Long addressId);
}
