package com.example.book_your_seat.concert.controller.dto;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.seat.domain.Seat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public final class ConcertResponse {
    private final Long id;
    private final String title;
    private final Integer totalStock;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer price;
    private final Integer startHour;
    private final LocalDateTime reservationStartAt;

    private ConcertResponse(final Concert concert) {
        this.id = concert.getId();
        this.title = concert.getTitle();
        this.totalStock = concert.getTotalStock();
        this.startDate = concert.getStartDate();
        this.endDate = concert.getEndDate();
        this.price = concert.getPrice();
        this.startHour = concert.getStartHour();
        this.reservationStartAt = concert.getReservationStartAt();
    }

    public static ConcertResponse from(final Concert concert) {
        return new ConcertResponse(concert);
    }
}
