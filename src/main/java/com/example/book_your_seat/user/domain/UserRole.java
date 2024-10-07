package com.example.book_your_seat.user.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN")
    ;

    private final String name;
    UserRole(String name) {
        this.name = name;
    }
}
