package com.example.book_your_seat.user.repository;

import com.example.book_your_seat.user.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
