package com.example.book_your_seat;

import com.example.book_your_seat.payment.controller.TossApiService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class IntegerTestSupport {

    @MockBean
    protected TossApiService tossApiService;
}
