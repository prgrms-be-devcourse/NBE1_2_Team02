package com.example.book_your_seat.user.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.command.UserCommandService;
import com.example.book_your_seat.user.service.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final UserCommandService userCommandService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody JoinRequest joinRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userCommandService.join(joinRequest));
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userFacade.addAddress(user.getId(), addAddressRequest));
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<AddressResponse> deleteAddress(
            @LoginUser User user,
            @PathVariable("addressId") Long addressId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userFacade.deleteAddress(user.getId(), addressId));
    }

}
