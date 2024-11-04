package com.example.book_your_seat.user.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    private String postcode;

    private String detail;

    public Address(String postcode, String detail) {
        this.postcode = postcode;
        this.detail = detail;
    }
}
