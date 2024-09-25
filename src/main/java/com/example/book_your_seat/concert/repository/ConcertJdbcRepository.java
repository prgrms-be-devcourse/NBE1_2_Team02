package com.example.book_your_seat.concert.repository;

import com.example.book_your_seat.concert.domain.Concert;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertJdbcRepository {

    Long saveBulkData(Concert concert);

    void deleteBulkData(Long concertId);
}
