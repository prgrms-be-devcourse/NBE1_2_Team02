package com.example.book_your_seat.querycounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.book_your_seat.aop.querycounter.QueryCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryCounterTest {

    private QueryCounter queryCounter;

    @BeforeEach
    void setUp() {
        // given
        queryCounter = new QueryCounter();
    }

    @Test
    @DisplayName("카운트 증가 테스트")
    void increase_shouldIncrementCount() {
        // when
        queryCounter.increase();

        // then
        assertEquals(1, queryCounter.getCount());
    }

    @Test
    @DisplayName("카운트가 10 이하일 경우 경고 하지 않는다.")
    void isWarn_shouldReturnFalseWhenCountIs10OrLess() {
        // given
        for (int i = 0; i < 10; i++) {
            queryCounter.increase();
        }

        // when
        boolean warn = queryCounter.isWarn();

        // then
        assertFalse(warn);
    }

    @Test
    @DisplayName("카운트가 10 이상일 경우 경고 한다.")
    void isWarn_shouldReturnTrueWhenCountIsGreaterThan10() {
        // given
        for (int i = 0; i < 11; i++) {
            queryCounter.increase();
        }

        // when
        boolean warn = queryCounter.isWarn();

        // then
        assertTrue(warn);
    }
}
