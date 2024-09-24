package com.example.book_your_seat.user.controller.dto;

import jakarta.validation.constraints.NotNull;

public record AddAddressRequest(

        @NotNull(message = "우편 번호를 입력해주세요.")
        String postcode,

        @NotNull(message = "상세 주소를 입력해주세요.")
        String detail

) {
}
