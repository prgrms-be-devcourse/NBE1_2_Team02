package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.seat.domain.Seat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertReservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    public ConcertReservation(Reservation reservation, Concert concert, Seat seat) {
        this.reservation = reservation;
        this.concert = concert;
        this.seat = seat;
        concert.addConcertReservation(this);
        reservation.addConcertReservation(this);
    }

}
