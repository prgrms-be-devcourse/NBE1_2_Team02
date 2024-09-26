package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 약식 방법 입니다.
 * 제대로 구현하려면 JDBC를 활용해서 connection 을 분리해야 합니다.
 */
public interface LockRepository extends JpaRepository<Coupon, Long> {

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);

}
