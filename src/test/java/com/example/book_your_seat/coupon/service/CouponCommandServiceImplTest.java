package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponCommandServiceImplTest {

    @Autowired
    private CouponCommandService couponCommandService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    private Coupon coupon;


    @BeforeEach
    void setUp() {
        // given
        coupon = new Coupon(100, DiscountRate.FIFTEEN);

        user = new User("nickname", "username", "test@test.com", "passwordpassword");
        userRepository.save(user);

        couponRepository.save(coupon);
    }




    @Test
    @DisplayName("쿠폰 발급 테스트")
    void coupon() throws Exception {
       //given
        couponCommandService.useCoupon(user, coupon.getId());

       //when
        entityManager.flush();
        entityManager.clear();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();

        //then
        Assertions.assertThat(findCoupon.getAmount()).isEqualTo(99);
    }

    @Test
    @DisplayName("쿠폰 발급 두번할때 예외처리")
    public void couponException() throws Exception {
       //given
        couponCommandService.useCoupon(user, coupon.getId());


        //when
        entityManager.flush();
        entityManager.clear();

       //then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            couponCommandService.useCoupon(user, coupon.getId());
        });
    }

}