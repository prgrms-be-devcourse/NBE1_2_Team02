package com.example.book_your_seat.payment.service.command.impl;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.payment.controller.PaymentClient;
import com.example.book_your_seat.payment.controller.dto.PaymentSuccessRequest;
import com.example.book_your_seat.payment.controller.dto.PaymentSuccessResponse;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmErrorResponse;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import com.example.book_your_seat.payment.service.command.PaymentCommandService;
import com.example.book_your_seat.reservation.controller.dto.BookReservationDto;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Base64;

import static com.example.book_your_seat.payment.PaymentConst.COUPON_NOT_FOUND;
import static com.example.book_your_seat.payment.domain.PaymentStatus.COMPLETED;

@Component
@Transactional
@RequiredArgsConstructor
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final CouponRepository couponRepository;
    private final ReservationCommandService reservationCommandService;
    private final PaymentClient paymentClient;
    private static final String SECRET_KEY = "yourSecretKey";
    @Override
    public PaymentSuccessResponse paymentSuccess(final PaymentSuccessRequest request) {
        String auth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());

        TossPaymentConfirmSuccessResponse response;
        try {
            response = paymentClient.confirmPayment("Basic " + auth, TossPaymentConfirmRequest.from(request));
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                TossPaymentConfirmErrorResponse errorResponse = objectMapper.readValue(exception.getResponseBodyAsString(), TossPaymentConfirmErrorResponse.class);
                throw new IllegalArgumentException(errorResponse.message());
            } catch (JsonProcessingException jsonEx) {
                throw new IllegalStateException("Failed to parse error response", jsonEx);
            }
        }

        Coupon coupon = couponRepository.findById(request.couponId())
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_FOUND));

        Payment payment = Payment.builder()
                .paymentStatus(COMPLETED)
                .tossPaymentOrderId(response.orderId())
                .discountRate(coupon.getDiscountRate())
                .expiryAt(LocalDateTime.now())
                .totalPrice(response.totalAmount())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        Reservation reservation = reservationCommandService.bookReservation(
                BookReservationDto.from(request.seatIds(), request.addressId(), request.userId(), request.couponId(), savedPayment)
        );
        return new PaymentSuccessResponse(response.orderId(), reservation);
    }
    //private final WebClient webClient;

    /*@Override
    public Mono<PaymentSuccessResponse> paymentSuccess(final PaymentSuccessRequest request) {
        return webClient
                .post()
                .uri("/payments/confirm")
                .bodyValue(TossPaymentConfirmRequest.from(request))
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(TossPaymentConfirmErrorResponse.class)
                                .flatMap(error -> Mono.error(new IllegalArgumentException(error.message())))
                )
                .bodyToMono(TossPaymentConfirmSuccessResponse.class)
                .flatMap(success -> {
                    Coupon coupon = couponRepository.findById(request.couponId())
                            .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_FOUND));

                    Payment payment = Payment.builder()
                            .paymentStatus(COMPLETED)
                            .tossPaymentOrderId(success.orderId())
                            .discountRate(coupon.getDiscountRate())
                            .expiryAt(LocalDateTime.now())
                            .totalPrice(success.totalAmount())
                            .build();

                    Payment savedPayment = paymentRepository.save(payment);

                    Reservation reservation = reservationCommandService.bookReservation(
                            BookReservationDto.from(request.seatIds(), request.addressId(), request.userId(), request.couponId(), savedPayment)
                    );

                    return Mono.just(new PaymentSuccessResponse(success.orderId(), reservation));
                })
                .onErrorMap(error -> new IllegalArgumentException(ENTER_PAYMENT_FAILED));
    }*/
}
