package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.common.event.Events;
import com.example.book_your_seat.seat.domain.SeatId;
import com.example.book_your_seat.user.domain.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.book_your_seat.reservation.domain.ReservationStatus.CANCELLED;
import static com.example.book_your_seat.reservation.domain.ReservationStatus.ORDERED;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Long userId;

    @Embedded
    private Address address;

    private Long paymentId;

    @ElementCollection
    @Column(name = "seat_id")
    private List<SeatId> seatIds;

    public void cancelReservation() {
        this.status = CANCELLED;
    }

    @Builder
    public Reservation(Long userId, Address address, Long paymentId, List<SeatId> seatIds) {
        this.userId = userId;
        this.address = address;
        this.paymentId = paymentId;
        this.seatIds = seatIds;
        this.status = ORDERED;
    }
}
