package com.example.book_your_seat.service.payment;

import com.example.DbCleaner;
import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.FinalPriceResponse;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import com.example.book_your_seat.payment.service.facade.PaymentFacade;
import com.example.book_your_seat.reservation.contorller.dto.PaymentRequest;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class PaymentServiceTest extends IntegralTestSupport {

    @Autowired
    DbCleaner dbCleaner;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private ConcertCommandService concertCommandService;

    @AfterEach
    void tearDown() {
        dbCleaner.cleanDatabase();
    }

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
        FinalPriceResponse response = paymentFacade.getFinalPrice(request);

        // then
        Assertions.assertThat(response.finalPrice()).isEqualTo(BigDecimal.valueOf(2999));
    }

    @DisplayName("Toss 에서 결제가 성공하면 결제 기록을 저장하고 결제된 정보를 사용자에게 전달한다")
    @Test
    void processPaymentTest() {

        // Given
        PaymentRequest request = new PaymentRequest(
                "paymentKey",
                "orderId",
                18000L,
                List.of(1L, 2L),
                1L,
                1L,
                1L,
                1L
        );

        ConfirmResponse response = ConfirmResponse.builder()
                .userId(1L)
                .reservationId(1L)
                .concludePrice(18000L)
                .status(ReservationStatus.ORDERED)
                .concertTitle("title")
                .concertStartHour(2)
                .seatsId(List.of(1L, 2L))
                .build();

        TossConfirmResponse confirmResponse = new TossConfirmResponse(
                "orderId",
                18000L,
                "paymentKey",
                LocalDateTime.now()
        );

        Concert concert = new Concert(
                "title",
                LocalDate.now().plusDays(30),
                LocalDate.now().plusDays(60),
                10000,
                2
        );
        concertRepository.save(concert);

        User user = new User("khan_nickname", "khan_username", "khan_email", "password");
        User savedUser = userRepository.save(user);

        Address address = new Address("12345", "khan_detail", savedUser);
        addressRepository.save(address);

        Coupon coupon = new Coupon(100, DiscountRate.TEN, LocalDate.now().plusDays(10));
        Coupon savedCoupon = couponRepository.save(coupon);

        UserCoupon userCoupon = new UserCoupon(savedUser, savedCoupon);
        userCouponRepository.save(userCoupon);

        PaymentCommand command = PaymentCommand.from(request, confirmResponse);


        // When
        ConfirmResponse result = paymentFacade.processPayment(command);


        // Then
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }
}
