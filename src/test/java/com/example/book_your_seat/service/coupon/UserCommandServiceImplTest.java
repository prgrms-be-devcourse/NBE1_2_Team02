package com.example.book_your_seat.service.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil;
import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import com.example.book_your_seat.user.service.command.UserCommandService;
import com.example.book_your_seat.user.service.command.UserCommandServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.book_your_seat.user.service.facade.UserFacade;
import com.example.book_your_seat.user.service.query.UserQueryService;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class UserCommandServiceImplTest extends IntegralTestSupport {

    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SecurityJwtUtil securityJwtUtil;

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
        UserResponse response = userCommandService.join(joinRequest);

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
        assertThrows(IllegalArgumentException.class, () -> userCommandService.join(joinRequest));
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginWithValidDataTest() {
        JoinRequest joinRequest = new JoinRequest("nickname", "username", "test2@test.com", "passwordpassword");
        Long userId = userCommandService.join(joinRequest).userId();
        // given
        LoginRequest loginRequest = new LoginRequest("test2@test.com", "passwordpassword");

        // when
        TokenResponse response = userCommandService.login(loginRequest);
        String email = securityJwtUtil.getEmailByToken(response.accessToken());

        // then
        User testUser = userRepository.findByEmail(email).get();
        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 하면 실패합니다.")
    void loginWithInvalidCredentialsTest() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "wrongpassword");

        // when // then
        assertThrows(IllegalArgumentException.class, () -> userCommandService.login(loginRequest));
    }

    @Test
    @DisplayName("주소 추가 테스트")
    void AddAddressTest() {
        // given
        AddAddressRequest addAddressRequest = new AddAddressRequest("postcode", "detail");

        // when
        AddressIdResponse addressIdResponse = userFacade.addAddress(existingUser.getId(), addAddressRequest);

        // then
        assertThat(addressIdResponse.addressId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주소 삭제 테스트")
    void deleteAddressTest() {
        // given
        AddAddressRequest addAddressRequest = new AddAddressRequest("postcode", "detail");
        AddressIdResponse addressIdResponse = userFacade.addAddress(existingUser.getId(), addAddressRequest);

        // when
        userFacade.deleteAddress(existingUser.getId(), addressIdResponse.addressId());

        // then
        Optional<Address> byId = addressRepository.findById(addressIdResponse.addressId());

        assertThat(byId.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("유저의 주소 목록을 반환한다.")
    void getUserAddressListTest() {
        // given
        AddAddressRequest addAddressRequest = new AddAddressRequest("postcode", "detail");
        userFacade.addAddress(existingUser.getId(), addAddressRequest);
        AddAddressRequest addAddressRequest2 = new AddAddressRequest("postcode2", "detail2");
        userFacade.addAddress(existingUser.getId(), addAddressRequest2);

        // when
        List<AddressResponse> list = userQueryService.getUserAddressList(existingUser);

        // then
        assertEquals(2, list.size());
        Assertions.assertThat(list)
                .extracting("postcode")
                .containsExactly("postcode", "postcode2");
    }
}
