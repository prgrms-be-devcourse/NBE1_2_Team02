package com.example.book_your_seat.likeconcert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "idx_like_concert_user_id", columnList = "like_concert_user_id"),
        @Index(name = "idx_like_concert_concert_id", columnList = "like_concert_concert_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeConcert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_concert_id")
    private Long id;

    private Long userId;
    private Long concertId;

    public LikeConcert(Long userId, Long concertId) {
        this.userId = userId;
        this.concertId = concertId;
    }
}
