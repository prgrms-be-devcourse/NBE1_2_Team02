package com.example.book_your_seat;

import com.example.DbCleaner;
import com.example.book_your_seat.config.JpaAuditingConfig;
import com.example.book_your_seat.payment.controller.TossApiService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(DbCleaner.class)
@SpringBootTest
@MockBean (JpaAuditingConfig.class)
public abstract class IntegralTestSupport {

    @MockBean
    protected TossApiService tossApiService;


}
