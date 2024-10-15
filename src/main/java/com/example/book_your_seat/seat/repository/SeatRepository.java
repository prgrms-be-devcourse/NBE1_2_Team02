package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import feign.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "SELECT * FROM Seat WHERE concert_id = :concertId", nativeQuery = true)
    List<Seat> findByConcertId(Long concertId);

    @Lock(LockModeType.PESSIMISTIC_READ)
//    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    List<Seat> findAllByIdWithLock(List<Long> seatIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    List<Seat> findAllByIdWithWrite(List<Long> seatIds);


//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Modifying
    @Query("UPDATE Seat s SET s.isSold = true WHERE s.id IN :seatIds AND s.isSold = false ")
    int findAllByIdWithRefactor(@Param("seatIds") List<Long> seatIds);


    @Query("SELECT s FROM Seat  s WHERE s.id IN :seatIds")
    List<Seat> findAllById(List<Long> seatIds);

    @Query("SELECT s FROM Seat s WHERE s.id IN :seatsId AND s.isSold = false")
    List<Seat> findValidSeats(List<Long> seatsId);
}
