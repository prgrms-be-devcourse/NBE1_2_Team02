package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.config.security.auth.CustomUserDetails;
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil;
import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
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

import java.util.List;

import static com.example.book_your_seat.user.UserConst.ADDRESS_NOT_OWNED;
import static com.example.book_your_seat.user.UserConst.INVALID_LOGIN_REQUEST;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final SecurityJwtUtil securityJwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserResponse join(JoinRequest joinRequest) {
        User user = new User(
                joinRequest.nickname(),
                joinRequest.username(),
                joinRequest.email(),
                bCryptPasswordEncoder.encode(joinRequest.password())
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId());
    }

    @Transactional
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
    
    public TokenResponse changeRoleToAdmin(User user) {
        user.changeRoleToAdmin();
        userRepository.save(user);
        return new TokenResponse(securityJwtUtil.createJwt(user));
    }

    public Address addAddress(User user, Address address) {
        user.addAddress(address);
        return address;
    }

    public void deleteAddress(User user, Address address) {
        List<Address> addressList = user.getAddressList();
        if(!addressList.contains(address)) {
            throw new IllegalArgumentException(ADDRESS_NOT_OWNED);
        }
        addressList.remove(address);
    }

}
