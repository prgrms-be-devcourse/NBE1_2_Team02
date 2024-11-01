package com.example.book_your_seat.likeconcert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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
