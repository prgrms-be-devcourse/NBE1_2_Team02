package com.example.book_your_seat.review.domain;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

    private int starCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    public Review(String content, int starCount, User user, Concert concert) {
        this.content = content;
        this.starCount = starCount;
        this.user = user;
        this.concert = concert;
        user.addReview(this);
        concert.addReview(this);
    }


    public static Review from(String content, int starCount, User user, Concert concert){
        return new Review(content, starCount, user, concert);
    }
}
