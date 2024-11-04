package com.example.book_your_seat.review.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "inx_reviews", columnList = "concert_id, created_at"),
        @Index(name = "inx_user_create", columnList = "user_id, created_at")
})

public class Review extends BaseEntity {

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


    public void updateReview(String content, int starCount){
        this.content = content;
        this.starCount = starCount;
    }
}
