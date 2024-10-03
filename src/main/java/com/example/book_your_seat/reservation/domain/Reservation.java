package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.seat.domain.Seat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    private UUID id;

    private Long userId;

    private Long addressId;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private final List<Seat> seats = new ArrayList<>();


    @Builder
    public Reservation(
            UUID id,
            Long userId,
            Long addressId,
            List<Seat> seats
    ) {
        this.id = id;
        this.userId = userId;
        this.addressId = addressId;

        for (Seat seat : seats) {
            addSeat(seat);
        }
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
        seat.addReservation(this);
    }

    public void addPayment(Payment payment) {
        this.status = ReservationStatus.ORDERED;
        this.payment = payment;
    }

    public boolean isNotOrdered() {
        return this.status == null;
    }
}
