package com.example.book_your_seat.seat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatId implements Serializable {

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "seat_number")
    private Long seatNumber;

    public SeatId(Long concertId, Long seatNumber) {
        this.concertId = concertId;
        this.seatNumber = seatNumber;
    }
}
