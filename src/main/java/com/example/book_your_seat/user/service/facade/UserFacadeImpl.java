package com.example.book_your_seat.user.service.facade;

import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressIdResponse;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.query.AddressQueryService;
import com.example.book_your_seat.user.service.query.UserQueryService;
import com.example.book_your_seat.user.service.command.AddressCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.user.UserConst.ADDRESS_NOT_OWNED;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserQueryService userQueryService;
    private final AddressQueryService addressQueryService;
    private final AddressCommandService addressCommandService;

    public AddressIdResponse addAddress(Long userId, AddAddressRequest addAddressRequest) {
        User user = userQueryService.getUserByUserId(userId);
        return addressCommandService.addAddress(user, addAddressRequest);
    }

    public AddressIdResponse deleteAddress(Long userId, Long addressId) {
        Address address = addressQueryService.getAddressWithUser(addressId);

        if(!userId.equals(address.getUser().getId()))
            throw new IllegalArgumentException(ADDRESS_NOT_OWNED);

        return addressCommandService.deleteAddress(addressId);
    }
}
