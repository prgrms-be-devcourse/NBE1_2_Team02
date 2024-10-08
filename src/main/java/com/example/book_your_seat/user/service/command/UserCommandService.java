package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.user.controller.dto.*;

public interface UserCommandService {

    UserResponse join(JoinRequest joinRequest);

    TokenResponse login(LoginRequest loginRequest);

}
