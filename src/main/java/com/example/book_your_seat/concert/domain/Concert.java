package com.example.book_your_seat.concert.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
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

    public Concert(String title, int totalStock, LocalDate startDate, LocalDate endDate, int price, int time) {
        this.title = title;
        this.totalStock = totalStock;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.time = time;
    }
}
