package com.example.book_your_seat.user.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.command.UserCommandService;
import com.example.book_your_seat.user.service.facade.UserFacade;
import com.example.book_your_seat.user.service.query.UserQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody JoinRequest joinRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userFacade.join(joinRequest));
    }

    @GetMapping("/email/cert")
    public ResponseEntity<Boolean> sendCertMail(
            @RequestParam("email") @NotNull @Email String email) {
        return ResponseEntity.ok(userFacade.sendCertMail(email));
    }

    @GetMapping("/email/check")
    public ResponseEntity<Boolean> checkCertCode(
            @RequestParam("email") @NotNull @Email String email,
            @RequestParam("certCode") @NotNull String certCode) {
        return ResponseEntity.ok(userFacade.checkCertCode(email, certCode));
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        TokenResponse tokenResponse = userCommandService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokenResponse);
    }

    @PostMapping("/address")
    public ResponseEntity<AddressResponse> addAddress(
            @LoginUser User user,
            @Valid @RequestBody AddAddressRequest addAddressRequest
    ) {
        Address address = userFacade.addAddress(user.getId(), addAddressRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AddressResponse.from(address));
    }

    @DeleteMapping("/address")
    public ResponseEntity<Void> deleteAddress(
            @LoginUser User user,
            @RequestBody DeleteAddressRequest request
    ) {
        userFacade.deleteAddress(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getUserAddressList(@LoginUser User user) {
        List<AddressResponse> responses = userQueryService.getUserAddressList(user.getId()).stream()
                .map(AddressResponse::from)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @PatchMapping("/role")
    public ResponseEntity<TokenResponse> changeRoleToAdminForTest(@LoginUser User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userFacade.changeRoleToAdminForTest(user.getId()));
    }
}
