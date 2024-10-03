package com.example.book_your_seat.payment;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.user.domain.User;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public final class ReservationResponse {

    private final Long userId;
    private final String reservationId;

    private final Long couponId;
    private final DiscountRate discountRate;
    private final LocalDate couponExpirationDate;
    private final Long concludePrice;
    private final ReservationStatus status;

    private final String concertTitle;
    private final int concertStartHour;
    private final List<Long> seatsId;


    @Builder
    private ReservationResponse(Long userId,
                                String reservationId,
                                Long couponId,
                                Long concludePrice,
                                ReservationStatus status,
                                DiscountRate discountRate,
                                LocalDate couponExpirationDate,
                                List<Long> seatsId,
                                String concertTitle,
                                int concertStartHour
    ) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.couponId = couponId;
        this.concludePrice = concludePrice;
        this.status = status;
        this.discountRate = discountRate;
        this.couponExpirationDate = couponExpirationDate;
        this.seatsId = seatsId;
        this.concertTitle = concertTitle;
        this.concertStartHour = concertStartHour;
    }

    public static ReservationResponse from(User user, Concert concert, Reservation reservation, Coupon coupon, Long concludedPrice) {
        return ReservationResponse.builder()
                .userId(user.getId())
                .reservationId(reservation.getId().toString())
                .concludePrice(concludedPrice)
                .status(reservation.getStatus())
                .discountRate(coupon.getDiscountRate())
                .couponExpirationDate(coupon.getExpirationDate())
                .seatsId(
                        reservation.getSeats().stream()
                        .map(Seat::getId)
                        .toList()
                )
                .concertTitle(concert.getTitle())
                .concertStartHour(concert.getStartHour())
                .build();
    }
}
