package com.example.book_your_seat.user.controller;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.service.UserCommandServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    private final UserCommandServiceImpl userCommandServiceImpl;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody JoinRequest joinRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userCommandServiceImpl.join(joinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        TokenResponse tokenResponse = userCommandServiceImpl.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokenResponse);
    }

    private void addMemberInSession(HttpServletRequest request, UserResponse loginUser) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_USER, loginUser);
    }

    @PostMapping("/{userId}/address")
    public ResponseEntity<AddressResponse> addAddress(
            @PathVariable("userId")Long userId,
            @Valid @RequestBody AddAddressRequest addAddressRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userCommandServiceImpl.addAddress(userId, addAddressRequest));
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<AddressResponse> deleteAddress(
            @PathVariable("addressId") Long addressId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userCommandServiceImpl.deleteAddress(addressId));
    }
}
