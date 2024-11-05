package com.example.book_your_seat.seat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @EmbeddedId
    private SeatId id;

    @Enumerated(EnumType.STRING)
    private Zone zone;

    private boolean isSold;

    public Seat(SeatId id) {
        this.id = id;
        this.zone = Zone.setZone(id.getSeatNumber());
        this.isSold = false;
    }

    public void selectSeat() {
        isSold = true;
    }

    public void releaseSeat() {
        isSold = false;
    }
}
