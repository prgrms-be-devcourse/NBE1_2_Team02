package com.example.book_your_seat.seat.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    private boolean isSold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Seat(final Concert concert) {
        this.isSold = false;
        this.concert = concert;
        concert.addSeat(this);
    }

    public void selectSeat() {
        isSold = true;
    }

    public void assignReservation(final Reservation reservation) {
        this.reservation = reservation;
        reservation.addSeat(this);
    }

    public void releaseSeat() {
        isSold = false;
    }
}
