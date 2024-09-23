package com.example.book_your_seat.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record JoinRequest(

        @NotNull
        @Length(min = 3, max = 20, message = "3~20 글자로 작성해주세요.")
        String nickname,

        @NotNull
        @Length(min = 3, max = 20, message = "3~20 글자로 작성해주세요.")
        String username,

        @NotNull
        @Email(message = "이메일 형식으로 입력해주세요.")
        String email,

        @NotNull
        @Length(min = 10, message = "10 글자 이상으로 작성해주세요.")
        String password,

        @NotNull(message = "우편 번호를 입력해주세요.")
        String postcode,

        @NotNull(message = "상세 주소를 입력해주세요.")
        String detail
) {
}
