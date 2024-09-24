package com.example.book_your_seat.concert.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.reservation.domain.ConcertReservation;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    private String title;
    private int totalStock;

    private LocalDate startDate;
    private LocalDate endDate;

    private int price;
    private int time;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private final List<LikeConcert> likeConcerts = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private final List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private final List<ConcertReservation> concertReservations = new ArrayList<>();

    public Concert(String title, int totalStock, LocalDate startDate, LocalDate endDate, int price, int time) {
        this.title = title;
        this.totalStock = totalStock;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.time = time;
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

    public void addConcertReservation(ConcertReservation reservation) {
        this.concertReservations.add(reservation);
    }
}
