package com.example.book_your_seat.user.service;

import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressQueryServiceImpl implements AddressQueryService {

    private final AddressRepository addressRepository;

    @Override
    public Address getAddressByAddressId(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_ADDRESS"));
    }
}
