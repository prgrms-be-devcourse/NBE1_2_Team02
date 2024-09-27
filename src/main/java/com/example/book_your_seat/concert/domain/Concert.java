package com.example.book_your_seat.concert.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.seat.domain.Seat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private final List<LikeConcert> likeConcerts = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Seat> seats = new ArrayList<>();

    public Concert(String title, LocalDate startDate, LocalDate endDate, int price, int startHour) {
        this.title = title;
        this.totalStock = TOTAL_STOCK;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.startHour = startHour;
        this.reservationStartAt = setReservationTime(startDate);
        initializeSeats(); // 혹시라도 Seat 가 100개를 초과하지 않을까
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

    private void initializeSeats() {
        IntStream.range(ZERO, TOTAL_STOCK)
                .forEach(i -> new Seat(this));
    }

    public void addLikeConcert(LikeConcert likeConcert) {
        this.likeConcerts.add(likeConcert);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

}
