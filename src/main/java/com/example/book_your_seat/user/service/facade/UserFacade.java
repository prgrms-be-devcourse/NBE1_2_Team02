package com.example.book_your_seat.user.service.facade;

import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.mail.service.MailService;
import com.example.book_your_seat.user.service.command.UserCommandService;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserFacade {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final MailService mailService;

    public UserResponse join(JoinRequest joinRequest) {
        mailService.checkVerifiedEmail(joinRequest.email());
        return userCommandService.join(joinRequest);
    }

    public Address addAddress(Long userId, AddAddressRequest addAddressRequest) {
        User user = userQueryService.getUserByUserId(userId);
        Address address = addAddressRequest.to();

        return userCommandService.addAddress(user, address);
    }

    public void deleteAddress(Long userId, DeleteAddressRequest request) {
        User user = userQueryService.getUserByUserId(userId);
        Address address = request.to();
        userCommandService.deleteAddress(user, address);
    }

    public Boolean sendCertMail(String email) {
        userQueryService.checkEmail(email);
        return mailService.sendCertMail(email);
    }

    public Boolean checkCertCode(String email, String certCode) {
        return mailService.checkCertCode(email, certCode);
    }

    public TokenResponse changeRoleToAdminForTest(Long userId) {
        User user = userQueryService.getUserByUserId(userId);
        return userCommandService.changeRoleToAdmin(user);
    }
}
