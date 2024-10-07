package com.example.book_your_seat.user.service.command;

import com.example.book_your_seat.user.controller.dto.AddAddressRequest;
import com.example.book_your_seat.user.controller.dto.AddressIdResponse;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressCommandServiceImpl implements AddressCommandService {

    private final AddressRepository addressRepository;

    @Override
    public AddressIdResponse addAddress(User user, AddAddressRequest addAddressRequest) {

        Address address = new Address(addAddressRequest.postcode(), addAddressRequest.detail(), user);
        Address savedAddress = addressRepository.save(address);
        return new AddressIdResponse(savedAddress.getId());
    }

    @Override
    public AddressIdResponse deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
        return new AddressIdResponse(addressId);
    }

}
