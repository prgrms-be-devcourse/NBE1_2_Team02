package com.example.book_your_seat.user.controller;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressResponse;
import com.example.book_your_seat.user.controller.dto.JoinRequest;
import com.example.book_your_seat.user.controller.dto.LoginRequest;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import com.example.book_your_seat.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody JoinRequest joinRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.join(joinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        UserResponse loginUserId = userService.login(loginRequest);
        addMemberInSession(request, loginUserId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginUserId);
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
                .body(userService.addAddress(userId, addAddressRequest));
    }
}
