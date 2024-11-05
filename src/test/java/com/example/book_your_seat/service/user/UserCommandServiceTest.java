package com.example.book_your_seat.service.user;

import com.example.DbCleaner;
import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil;
import com.example.book_your_seat.user.controller.dto.*;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.mail.repository.MailRedisRepository;
import com.example.book_your_seat.user.mail.service.MailService;
import com.example.book_your_seat.user.repository.UserRepository;
import com.example.book_your_seat.user.service.command.UserCommandService;
import com.example.book_your_seat.user.service.facade.UserFacade;
import com.example.book_your_seat.user.service.query.UserQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.user.UserConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserCommandServiceTest extends IntegralTestSupport {

    @Autowired
    private DbCleaner dbCleaner;

    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailRedisRepository mailRedisRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private SecurityJwtUtil securityJwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User("nickname", "username", "test@test.com", "passwordpassword");
        userRepository.save(existingUser);
    }

    @AfterEach
    void tearDown() {
        dbCleaner.cleanDatabase();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("회원 가입에 성공한다.")
    void joinWithValidDataTest() {
        // given
        mailRedisRepository.saveVerifiedEmail("test2@test.com");
        JoinRequest joinRequest = new JoinRequest("nickname", "username", "test2@test.com", "passwordpassword");

        // when
        UserResponse response = userFacade.join(joinRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(userRepository.findById(response.userId())).isPresent();
    }

    @Test
    @DisplayName("인증되지 않은 이메일로 회원가입 할 경우, 예외가 발생한다.")
    void joinEmailNotVerifiedTest() {
        // given
        JoinRequest joinRequest = new JoinRequest("nickname", "username", "test2@test.com", "passwordpassword");

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userFacade.join(joinRequest);
        });

        //then
        assertEquals(EMAIL_NOT_VERIFIED, exception.getMessage());
    }

    @Test
    @DisplayName("중복되는 이메일로는 가입 할 수 없다.")
    void joinWithDuplicateEmailTest() {
        // given
        String mail = "test@test.com";

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userFacade.sendCertMail(mail);
        });

        //then
        assertEquals(ALREADY_JOIN_EMAIL, exception.getMessage());
    }

    @Test
    @DisplayName("인증 코드가 올바르지 않으면 예외가 발생한다.")
    void checkCertCodeTest() {
        //given
        String mail = "test2@test.com";
        String certCode = "C12345";
        String certCode2 = "AABBC";
        mailRedisRepository.saveEmailCertCode(mail, certCode);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mailService.checkCertCode(mail, certCode2);
        });

        //then
        assertEquals(INVALID_CERT_CODE, exception.getMessage());
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void loginWithValidDataTest() {
        // given
        JoinRequest joinRequest = new JoinRequest("nickname", "username", "test2@test.com", "passwordpassword");
        Long userId = userCommandService.join(joinRequest).userId();
        LoginRequest loginRequest = new LoginRequest("test2@test.com", "passwordpassword");

        // when
        TokenResponse response = userCommandService.login(loginRequest);
        String email = securityJwtUtil.getEmailByToken(response.accessToken());

        // then
        User testUser = userRepository.findByEmail(email).get();
        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 하면 실패한다.")
    void loginWithInvalidCredentialsTest() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "wrongpassword");

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.login(loginRequest);
        });

        //then
        assertEquals(INVALID_LOGIN_REQUEST, exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("주소를 추가한다.")
    void AddAddressTest() {
        // given
        AddAddressRequest addAddressRequest = new AddAddressRequest("postcode", "detail");

        // when
        Address address = userFacade.addAddress(existingUser.getId(), addAddressRequest);
        List<Address> list = existingUser.getAddressList();

        // then
        assertThat(list).contains(address);
    }

    @Test
    @DisplayName("주소를 삭제한다.")
    void deleteAddressTest() {
        // given
        DeleteAddressRequest deleteRequest = new DeleteAddressRequest("postcode", "detail");
        AddAddressRequest addRequest = new AddAddressRequest("postcode", "detail");
        Address address = addRequest.to();
        userFacade.addAddress(existingUser.getId(), addRequest);

        // when
        userFacade.deleteAddress(existingUser.getId(), deleteRequest);
        List<Address> list = existingUser.getAddressList();

        // then
        assertThat(list).doesNotContain(address);
    }

    @Test
    @DisplayName("본인의 주소가 아니면 삭제할 수 없다.")
    void deleteAddressFailTest() {
        // given
        User newUser = new User("nickname", "username", "test@test.com", "passwordpassword");
        userRepository.saveAndFlush(newUser);

        DeleteAddressRequest request = new DeleteAddressRequest("postcode", "detail");

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userFacade.deleteAddress(existingUser.getId(), request));

        //then
        assertEquals(ADDRESS_NOT_OWNED, exception.getMessage());
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
        List<Address> userAddressList = userQueryService.getUserAddressList(existingUser.getId());

        // then
        Assertions.assertThat(userAddressList)
                .extracting("postcode")
                .containsExactly("postcode", "postcode2");
    }
}
