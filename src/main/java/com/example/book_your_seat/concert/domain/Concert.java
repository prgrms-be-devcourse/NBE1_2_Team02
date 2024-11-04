package com.example.book_your_seat.concert.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.book_your_seat.concert.ConcertConst.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    private String title;

    private Integer totalStock;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer price;

    private int startHour;  // Hour, 시작시간

    private LocalDateTime reservationStartAt;

    public Concert(String title, LocalDate startDate, LocalDate endDate, int price, int startHour) {
        this.title = title;
        this.totalStock = TOTAL_STOCK;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.startHour = startHour;
        this.reservationStartAt = setReservationTime(startDate);
    }

    private LocalDateTime setReservationTime(LocalDate startDate) {
        return LocalDateTime.of(
                startDate.getYear(),
                startDate.getMonth(),
                startDate.getDayOfMonth(),
                RESERVATION_START_HOUR,
                RESERVATION_START_MINUTE,
                RESERVATION_START_SECOND
        )
                .minusWeeks(1);
    }

}
