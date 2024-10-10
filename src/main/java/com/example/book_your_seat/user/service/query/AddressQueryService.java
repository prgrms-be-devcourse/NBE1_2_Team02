package com.example.book_your_seat.user.service.query;

import com.example.book_your_seat.user.domain.Address;

public interface AddressQueryService {

    Address getAddressWithUser(Long addressId);

}
