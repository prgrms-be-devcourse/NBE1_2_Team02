package com.example.book_your_seat.user.service.facade;

import com.example.book_your_seat.user.controller.dto.*;

import java.util.concurrent.CompletableFuture;


public interface UserFacade {

    UserResponse join(JoinRequest joinRequest);

    AddressIdResponse addAddress(Long userId, AddAddressRequest addAddressRequest);

    AddressIdResponse deleteAddress(Long userId, Long addressId);

    Boolean sendCertMail(String mail);

    Boolean checkCertCode(String mail, String certCode);

}
