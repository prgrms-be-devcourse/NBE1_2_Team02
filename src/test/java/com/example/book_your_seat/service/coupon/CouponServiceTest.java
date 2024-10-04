package com.example.book_your_seat.service.coupon;

import static com.example.book_your_seat.coupon.CouponConst.ALREADY_ISSUED_USER;
import static org.assertj.core.api.Assertions.*;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.coupon.facade.CouponCommandService;
import com.example.book_your_seat.coupon.facade.CouponQueryService;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CouponServiceTest extends IntegralTestSupport {


    @Autowired
    private CouponCommandService couponCommandService;

    @Autowired
    private CouponQueryService couponQueryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    private User savedUser;

    @BeforeEach
    void beforeEach() {
        User user = new User("nickname", "username", "email@email.com", "password123456789");
        savedUser = userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        couponRepository.deleteAll();
        userCouponRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰 생성 성공")
    void createCouponTest() {
        // given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10, LocalDate.now().plusDays(2));

        // when
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        // then
        Optional<Coupon> byId = couponRepository.findById(coupon.couponId());

        assertThat(byId.isEmpty()).isFalse();
        assertThat(byId.isPresent()).isTrue();

        Coupon savedCoupon = byId.get();
        assertThat(savedCoupon.getAmount()).isEqualTo(couponCreateRequest.quantity());
        assertThat(savedCoupon.getDiscountRate().getValue()).isEqualTo(couponCreateRequest.discountRate());
    }

    @Test
    @DisplayName("쿠폰을 한 개 생성한다.")
    void issueCouponTest() {
        //given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10, LocalDate.now().plusDays(2));
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        //when
        UserCouponIdResponse userCouponIdResponse = couponCommandService.issueCouponWithPessimistic(savedUser.getId(),
                coupon.couponId());

        //then
        Optional<UserCoupon> byId = userCouponRepository.findById(userCouponIdResponse.userCouponId());
        assertThat(byId.isPresent()).isTrue();

        boolean shouldBeTrue = userCouponRepository.existsByUserIdAndCouponId(savedUser.getId(), coupon.couponId());
        assertThat(shouldBeTrue).isTrue();
    }

    @Test
    @DisplayName("쿠폰 중복 발급 시도시 예외 처리")
    void issueCouponFailTest() {
        //given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10, LocalDate.now().plusDays(2));
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        //when & then
        couponCommandService.issueCouponWithPessimistic(savedUser.getId(), coupon.couponId());

        assertThatThrownBy(() -> couponCommandService.issueCouponWithPessimistic(savedUser.getId(), coupon.couponId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_ISSUED_USER);

    }

    @Test
    @DisplayName("로그인한 유저의 보유 쿠폰 목록을 조회한다.")
    void getCouponDetailsTest() {
        //given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10, LocalDate.now().plusDays(2));
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        CouponCreateRequest couponCreateRequest2 = new CouponCreateRequest(100, 5, LocalDate.now().plusDays(2));
        CouponResponse coupon2 = couponCommandService.createCoupon(couponCreateRequest2);

        couponCommandService.issueCouponWithPessimistic(savedUser.getId(), coupon.couponId());
        couponCommandService.issueCouponWithPessimistic(savedUser.getId(), coupon2.couponId());

        //when
        List<CouponDetailResponse> couponDetail = couponQueryService.getCouponDetail(savedUser.getId());

        //then
        assertThat(couponDetail.size()).isEqualTo(2);
        assertThat(couponDetail.get(0).discountRate()).isEqualTo("10%");
        assertThat(couponDetail.get(1).discountRate()).isEqualTo("5%");

    }

}
