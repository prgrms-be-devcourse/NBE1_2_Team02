package com.example.book_your_seat.concert.domain;

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
public class LikeConcert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_concert_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    public LikeConcert(User user, Concert concert) {
        this.user = user;
        this.concert = concert;
        user.addLikeConcert(this);
        concert.addLikeConcert(this);
    }
}
