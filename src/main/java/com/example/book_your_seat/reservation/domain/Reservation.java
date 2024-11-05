package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.seat.domain.SeatId;
import com.example.book_your_seat.user.domain.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.example.book_your_seat.reservation.domain.ReservationStatus.CANCELLED;
import static com.example.book_your_seat.reservation.domain.ReservationStatus.ORDERED;

@Entity
@Getter
@Table(indexes = @Index(name = "idx_reservation_user_id", columnList = "user_id"))
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

    private UUID paymentId;

    @ElementCollection
    @CollectionTable(name = "reservation_seat", joinColumns = @JoinColumn(name = "reservation_id"),
            indexes = @Index(name = "idx_reservation_seat_id", columnList = "reservation_id, concert_id, seat_number")
    )
    private List<SeatId> seatIds;

    public void cancelReservation() {
        this.status = CANCELLED;
    }

    @Builder
    public Reservation(Long userId, Address address, UUID paymentId, List<SeatId> seatIds) {
        this.userId = userId;
        this.address = address;
        this.paymentId = paymentId;
        this.seatIds = seatIds;
        this.status = ORDERED;
    }
}
