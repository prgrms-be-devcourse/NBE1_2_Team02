package com.example.book_your_seat.user.service.query;

import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.user.UserConst.ADDRESS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AddressQueryServiceImpl implements AddressQueryService {

    private final AddressRepository addressRepository;

    @Override
    public Address getAddressWithUser(Long addressId) {
        return addressRepository.findByIdWithUser(addressId)
                .orElseThrow(() -> new IllegalArgumentException(ADDRESS_NOT_FOUND));
    }

}
