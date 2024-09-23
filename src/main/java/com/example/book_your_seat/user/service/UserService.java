package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.controller.dto.JoinRequest;
import com.example.book_your_seat.user.controller.dto.LoginRequest;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public UserResponse join(JoinRequest joinRequest) {
        checkEmail(joinRequest.email());
        User user = new User(
                joinRequest.nickname(),
                joinRequest.username(),
                joinRequest.email(),
                joinRequest.password()
        );

        User savedUser = userRepository.save(user);

        Address address = new Address(joinRequest.postcode(), joinRequest.detail(), savedUser);
        addressRepository.save(address);

        return new UserResponse(savedUser.getId());
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입한 이메일입니다.");
        }
    }

    @Transactional
    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .filter(m -> m.getPassword().equals(loginRequest.password()))
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));
        return new UserResponse(user.getId());
    }

}
