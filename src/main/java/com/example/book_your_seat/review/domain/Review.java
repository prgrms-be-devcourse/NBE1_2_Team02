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

    private Long userId;

    private Long concertId;

    public Review(String content, int starCount, Long userId, Long concertId) {
        this.content = content;
        this.starCount = starCount;
        this.userId = userId;
        this.concertId = concertId;
    }

    public static Review from(String content, int starCount, Long userId, Long concertId){
        return new Review(content, starCount, userId, concertId);
    }


    public void updateReview(String content, int starCount){
        this.content = content;
        this.starCount = starCount;
    }
}
