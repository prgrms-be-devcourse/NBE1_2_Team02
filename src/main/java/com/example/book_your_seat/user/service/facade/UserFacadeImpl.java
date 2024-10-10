package com.example.book_your_seat.user.service.facade;

import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.mail.service.MailService;
import com.example.book_your_seat.user.service.command.AddressCommandService;
import com.example.book_your_seat.user.service.command.UserCommandService;
import com.example.book_your_seat.user.service.query.AddressQueryService;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.user.UserConst.ADDRESS_NOT_OWNED;


@Service
@Transactional
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final AddressQueryService addressQueryService;
    private final AddressCommandService addressCommandService;
    private final MailService mailService;

    @Override
    public UserResponse join(JoinRequest joinRequest) {
        mailService.checkVerifiedEmail(joinRequest.email());
        return userCommandService.join(joinRequest);
    }

    @Override
    public AddressIdResponse addAddress(Long userId, AddAddressRequest addAddressRequest) {
        User user = userQueryService.getUserByUserId(userId);
        return addressCommandService.addAddress(user, addAddressRequest);
    }

    @Override
    public AddressIdResponse deleteAddress(Long userId, Long addressId) {
        Address address = addressQueryService.getAddressWithUser(addressId);

        if(!userId.equals(address.getUser().getId()))
            throw new IllegalArgumentException(ADDRESS_NOT_OWNED);

        return addressCommandService.deleteAddress(addressId);
    }

    @Override
    public Boolean sendCertMail(String email) {
        userQueryService.checkEmail(email);
        return mailService.sendCertMail(email);
    }

    @Override
    public Boolean checkCertCode(String email, String certCode) {
        return mailService.checkCertCode(email, certCode);
    }

    @Override
    public TokenResponse changeRoleToAdminForTest(Long userId) {
        User user = userQueryService.getUserByUserId(userId);
        return userCommandService.changeRoleToAdmin(user);
    }
}
