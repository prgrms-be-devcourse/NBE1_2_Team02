package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.domain.SeatId;
import feign.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, SeatId>, SeatJdbcRepository{

    @Query(value = "SELECT * FROM Seat WHERE concert_id = :concertId", nativeQuery = true)
    List<Seat> findByConcertId(Long concertId);

    @Query(value = "SELECT s FROM Seat s WHERE s.id.concertId = :concertId AND s.id.seatNumber = :seatNumber")
    Optional<Seat> findByConcertIdAndSeatNo(Long concertId, Long seatNumber);


    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT s FROM Seat s WHERE s.id.concertId = :concertId AND s.id.seatNumber IN :seatNumbers")
    List<Seat> findAllByIdWithLock(Long concertId, List<Long> seatNumbers);

    @Query("SELECT s FROM Seat  s WHERE s.id.seatNumber IN :seatNumbers")
    List<Seat> findAllById(List<Long> seatNumbers);

    @Query("SELECT s FROM Seat s WHERE s.id IN :seatNumbers AND s.isSold = false")
    List<Seat> findValidSeats(List<Long> seatNumbers);

    @Modifying
    void deleteById_ConcertId(Long concertId);

    @Query("SELECT s FROM Seat s WHERE s.id.concertId = :concertId AND s.id.seatNumber IN :seatNumbers")
    List<Seat> findAllBySeatId(@Param("concertId") Long concertId, @Param("seatNumbers") List<Long> seatNumbers);
}
