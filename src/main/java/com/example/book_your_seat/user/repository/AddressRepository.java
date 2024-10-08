package com.example.book_your_seat.user.repository;

import com.example.book_your_seat.user.domain.Address;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a JOIN FETCH a.user u WHERE a.id = :addressId")
    Optional<Address> findByIdWithUser(@Param("addressId") Long addressId);

}
