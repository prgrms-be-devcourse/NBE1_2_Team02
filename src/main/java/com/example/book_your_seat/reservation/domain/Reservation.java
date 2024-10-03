package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_CONCERT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "user_coupon_id")
    private Long userCouponId;

    @Column(name = "final_price")
    private Integer finalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private final List<Seat> seats = new ArrayList<>();

    @Builder
    public Reservation(ReservationStatus status, Long userCouponId, Integer finalPrice, User user, Address address,Payment payment) {
        this.status = status;
        this.userCouponId = userCouponId;
        this.finalPrice = finalPrice;
        this.user = user;
        this.address = address;
        this.payment = payment;
        user.addReservation(this);
        address.addReservation(this);
        payment.addReservation(this);
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

    public Concert getConcert() {
        return this.seats.stream()
                .findFirst()
                .map(Seat::getConcert)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT));
    }

}
