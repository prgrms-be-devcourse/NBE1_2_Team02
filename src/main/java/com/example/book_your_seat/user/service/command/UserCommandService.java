package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.user.controller.dto.JoinRequest;
import com.example.book_your_seat.user.controller.dto.LoginRequest;
import com.example.book_your_seat.user.controller.dto.TokenResponse;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import com.example.book_your_seat.user.domain.User;

public interface UserCommandService {

    UserResponse join(JoinRequest joinRequest);

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse changeRoleToAdmin(User user);

}
