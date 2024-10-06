package com.example.book_your_seat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressResponse;
import com.example.book_your_seat.user.controller.dto.JoinRequest;
import com.example.book_your_seat.user.controller.dto.LoginRequest;
import com.example.book_your_seat.user.controller.dto.UserResponse;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import com.example.book_your_seat.user.service.UserCommandServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class UserCommandServiceImplTest extends IntegralTestSupport {

    @Autowired
    private UserCommandServiceImpl userCommandServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User existingUser;

    @BeforeEach
    void setUp() {
        // given
        existingUser = new User("nickname", "username", "test@test.com", "passwordpassword");
        userRepository.save(existingUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void joinWithValidDataTest() {
        // given
        JoinRequest joinRequest = new JoinRequest("nickname", "username", "test2@test.com", "passwordpassword");

        // when
        UserResponse response = userCommandServiceImpl.join(joinRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(userRepository.findById(response.userId())).isPresent();
    }

    @Test
    @DisplayName("중복되는 이메일로는 가입 할 수 없습니다.")
    void joinWithDuplicateEmailTest() {
        // given
        JoinRequest joinRequest = new JoinRequest("nickname", "username", "test@test.com", "passwordpassword");

        // when // then
        assertThrows(IllegalArgumentException.class, () -> userCommandServiceImpl.join(joinRequest));
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginWithValidDataTest() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "passwordpassword");

        // when
        UserResponse response = userCommandServiceImpl.login(loginRequest);

        // then
        assertThat(response.userId()).isEqualTo(existingUser.getId());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 하면 실패합니다.")
    void loginWithInvalidCredentialsTest() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "wrongpassword");

        // when // then
        assertThrows(IllegalArgumentException.class, () -> userCommandServiceImpl.login(loginRequest));
    }

    @Test
    @DisplayName("주소 추가 테스트")
    void AddAddressTest() {
        // given
        AddAddressRequest addAddressRequest = new AddAddressRequest("postcode", "detail");

        // when
        AddressResponse addressResponse = userCommandServiceImpl.addAddress(existingUser.getId(), addAddressRequest);

        // then
        assertThat(addressResponse.addressId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주소 삭제 테스트")
    void deleteAddressTest() {
        // given
        AddAddressRequest addAddressRequest = new AddAddressRequest("postcode", "detail");
        AddressResponse addressResponse = userCommandServiceImpl.addAddress(existingUser.getId(), addAddressRequest);

        // when
        userCommandServiceImpl.deleteAddress(addressResponse.addressId());

        // then
        Optional<Address> byId = addressRepository.findById(addressResponse.addressId());

        assertThat(byId.isEmpty()).isTrue();
    }
}
