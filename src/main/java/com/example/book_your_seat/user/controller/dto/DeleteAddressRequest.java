package com.example.book_your_seat.user.controller.dto;

import com.example.book_your_seat.user.domain.Address;
import lombok.Getter;

@Getter
public class DeleteAddressRequest {

    private final String postcode;

    private final String detail;

    public DeleteAddressRequest(String postcode, String detail) {
        this.postcode = postcode;
        this.detail = detail;
    }

    public Address to() {
        return new Address(postcode, detail);
    }
}
