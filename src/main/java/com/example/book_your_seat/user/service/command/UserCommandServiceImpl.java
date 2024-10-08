package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.config.security.auth.CustomUserDetails;
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil;
import com.example.book_your_seat.user.controller.dto.JoinRequest;
import com.example.book_your_seat.user.controller.dto.LoginRequest;
import com.example.book_your_seat.user.controller.dto.TokenResponse;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.user.UserConst.ALREADY_JOIN_EMAIL;
import static com.example.book_your_seat.user.UserConst.INVALID_LOGIN_REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityJwtUtil securityJwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserResponse join(JoinRequest joinRequest) {
        checkEmail(joinRequest.email());
        User user = new User(
                joinRequest.nickname(),
                joinRequest.username(),
                joinRequest.email(),
                bCryptPasswordEncoder.encode(joinRequest.password())
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId());
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        // 인증 요청 처리
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            String token = securityJwtUtil.createJwt(((CustomUserDetails) authentication.getPrincipal()).getUser());

            return new TokenResponse(token);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException(INVALID_LOGIN_REQUEST);
        }
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ALREADY_JOIN_EMAIL);
        }
    }

}
