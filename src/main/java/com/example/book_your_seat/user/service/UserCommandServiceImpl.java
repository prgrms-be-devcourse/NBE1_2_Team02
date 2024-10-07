package com.example.book_your_seat.user.service;

import com.example.book_your_seat.config.security.auth.CustomUserDetails;
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil;
import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.user.UserConst.INVALID_LOGIN_REQUEST;
import static com.example.book_your_seat.user.domain.QUser.user;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityJwtUtil securityJwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


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

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입한 이메일입니다.");
        }
    }

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

    public AddressResponse addAddress(Long userId, AddAddressRequest addAddressRequest) {
        User user = getUser(userId);
        Address address = new Address(addAddressRequest.postcode(), addAddressRequest.detail(), user);
        Address savedAddress = addressRepository.save(address);
        return new AddressResponse(savedAddress.getId());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    public AddressResponse deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
        return new AddressResponse(addressId);
    }
}
