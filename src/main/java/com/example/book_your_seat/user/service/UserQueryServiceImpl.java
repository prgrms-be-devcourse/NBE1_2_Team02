package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.user.AddressConst.ADDRESS_NOT_FOUND;
import static com.example.book_your_seat.user.UserConst.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(USER_NOT_FOUND)
        );
    }

    @Override
    public Address getAddress(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(
                () -> new IllegalArgumentException(ADDRESS_NOT_FOUND)
        );
    }
}
