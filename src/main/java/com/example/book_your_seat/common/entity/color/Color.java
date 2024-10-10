package com.example.book_your_seat.common.entity.color;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {

    RED("#ff0000"),

    BLUE("#0000FF");

    private final String code;

}
