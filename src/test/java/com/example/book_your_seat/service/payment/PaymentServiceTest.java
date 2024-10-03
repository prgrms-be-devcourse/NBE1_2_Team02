package com.example.book_your_seat.service.payment;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.payment.controller.dto.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.FinalPriceResponse;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class PaymentServiceTest extends IntegerTestSupport {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConcertRepository concertRepository;

    @Test
    @DisplayName("3333원 짜리 공연을 10%할인 쿠폰을 적용하여 최종금액 계산시 2999원")
    void getFinalPriceTest() {
        // given
        User user = userRepository.save(new User("1234", "!234", "1234", "1234"));

        Coupon coupon = couponRepository.save(new Coupon(1, DiscountRate.TEN, LocalDate.now().plusDays(30)));

        UserCoupon userCoupon = userCouponRepository.save(new UserCoupon(user, coupon));

        Concert concert = concertRepository.save(new Concert("1234", LocalDate.now(), LocalDate.now(), 3333, 1));

        Seat seat = seatRepository.save(new Seat(concert));

        FinalPriceRequest request = new FinalPriceRequest(Collections.singletonList(seat.getId()), userCoupon.getId());

        // when
        FinalPriceResponse response = paymentService.getFinalPrice(request);

        // then
        assertThat(response.finalPrice(), is(BigDecimal.valueOf(2999)));
    }
}