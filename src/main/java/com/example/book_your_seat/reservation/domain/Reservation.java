package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
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

import static com.example.book_your_seat.reservation.domain.ReservationStatus.CANCELLED;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

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
    public Reservation(ReservationStatus status, User user, Address address,Payment payment) {
        this.status = status;
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

    public void cancelReservation() {
        this.status = CANCELLED;
    }

}
