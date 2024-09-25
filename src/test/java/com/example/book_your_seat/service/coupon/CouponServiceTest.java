package com.example.book_your_seat.service.coupon;

import static org.assertj.core.api.Assertions.*;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.coupon.service.CouponQueryService;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CouponServiceTest extends IntegerTestSupport {

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
    }

    @Test
    @DisplayName("쿠폰 생성 성공")
    void createCouponTest() {
        // given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10);

        // when
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        // then
        Optional<Coupon> byId = couponRepository.findById(coupon.couponId());
        assertThat(byId.isPresent()).isTrue();

        Coupon savedCoupon = byId.get();
        assertThat(savedCoupon.getAmount()).isEqualTo(couponCreateRequest.quantity());
        assertThat(savedCoupon.getDiscountRate().getValue()).isEqualTo(couponCreateRequest.discountRate());
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    void issueCouponTest() {
        //given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10);
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        //when
        CouponResponse couponResponse = couponCommandService.issueCoupon(savedUser.getId(), coupon.couponId());

        //then
        assertThat(couponResponse.couponId()).isEqualTo(coupon.couponId());

        boolean shouldBeTrue = userCouponRepository.existsByUserIdAndCouponId(savedUser.getId(), coupon.couponId());
        assertThat(shouldBeTrue).isTrue();
    }

    @Test
    @DisplayName("쿠폰 중복 발급 시도시 예외 처리")
    void issueCouponFailTest() {
        //given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10);
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        //when & then
        couponCommandService.issueCoupon(savedUser.getId(), coupon.couponId());

        assertThatThrownBy(() -> couponCommandService.issueCoupon(savedUser.getId(), coupon.couponId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 쿠폰을 발급 받은 유저입니다.");

    }

    @Test
    @DisplayName("나의 쿠폰 조회 테스트")
    void getCouponDetailsTest() {
        //given
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest(100, 10);
        CouponResponse coupon = couponCommandService.createCoupon(couponCreateRequest);

        CouponCreateRequest couponCreateRequest2 = new CouponCreateRequest(100, 5);
        CouponResponse coupon2 = couponCommandService.createCoupon(couponCreateRequest2);

        couponCommandService.issueCoupon(savedUser.getId(), coupon.couponId());
        couponCommandService.issueCoupon(savedUser.getId(), coupon2.couponId());

        //when
        List<CouponDetailResponse> couponDetail = couponQueryService.getCouponDetail(savedUser.getId());

        //then
        assertThat(couponDetail.size()).isEqualTo(2);
        assertThat(couponDetail.get(0).discountRate()).isEqualTo("10%");
        assertThat(couponDetail.get(1).discountRate()).isEqualTo("5%");

    }

}
