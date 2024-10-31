package com.example.book_your_seat.concert.controller.dto;

import com.example.book_your_seat.concert.domain.Concert;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ConcertListResponse {

    private Long id;

    private String title;

    private Integer totalStock;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    private Integer price;

    private int startHour;  // Hour, 시작시간

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime reservationStartAt;


    public ConcertListResponse(Concert concert) {
        this.id = concert.getId();
        this.title = concert.getTitle();
        this.totalStock = concert.getTotalStock();
        this.startDate = concert.getStartDate();
        this.endDate = concert.getEndDate();
        this.price = concert.getPrice();
        this.startHour = concert.getStartHour();
        this.reservationStartAt = concert.getReservationStartAt();
    }
}