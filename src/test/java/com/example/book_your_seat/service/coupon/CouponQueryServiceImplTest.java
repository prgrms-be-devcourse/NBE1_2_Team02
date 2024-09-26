package com.example.book_your_seat.service.coupon;

import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.coupon.service.CouponQueryServiceImpl;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.example.book_your_seat.coupon.domain.DiscountRate.FIFTEEN;
import static com.example.book_your_seat.coupon.domain.DiscountRate.FIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

public class CouponQueryServiceImplTest {
    @Autowired
    private CouponQueryServiceImpl couponQueryService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    private User testUser;
    private Coupon testCoupon1;
    private Coupon testCoupon2;

    @BeforeEach
    public void setUp() {
        testUser = userRepository.saveAndFlush(new User("nickname", "username", "email@gmail.com", "userpassword"));
        testCoupon1 = couponRepository.saveAndFlush(new Coupon(100, FIVE, LocalDate.of(2024, 11, 01)));
        testCoupon2 = couponRepository.saveAndFlush(new Coupon(200, FIFTEEN, LocalDate.of(2024, 11, 01)));

    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        couponRepository.deleteAll();
        userCouponRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인한 유저의 보유 쿠폰 목록을 조회한다.")
    public void getUserCouponsTest() {
        //given & when
        userCouponRepository.save(new UserCoupon(testUser, testCoupon1));
        userCouponRepository.save(new UserCoupon(testUser, testCoupon2));

        List<UserCouponResponse> userCouponList = couponQueryService.getUserCoupons(testUser);

        //then
        assertEquals(2, userCouponList.size());

        assertThat(userCouponList).extracting(UserCouponResponse::userCouponId)
                .containsExactly(1L, 2L);

        assertThat(userCouponList).extracting(UserCouponResponse::discountRate)
                .containsExactly(FIVE, FIFTEEN);

        assertThat(userCouponList).extracting(UserCouponResponse::expirationDate)
                .containsExactly(LocalDate.of(2024, 11, 01), LocalDate.of(2024, 11, 01));

        assertThat(userCouponList).extracting(UserCouponResponse::isUsed)
                .containsExactly(false, false);
    }
}
