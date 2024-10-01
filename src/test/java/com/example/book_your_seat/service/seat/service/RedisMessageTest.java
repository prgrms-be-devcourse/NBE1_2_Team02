package com.example.book_your_seat.service.seat.service;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.RedisExpiredListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RedisMessageTest extends IntegerTestSupport {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private RedisMessageListenerContainer listenerContainer;

    @InjectMocks
    private RedisExpiredListener redisExpiredListener;

    private Message message;

    @BeforeEach
    public void setUp() {
        // Mock the Message object
        message = mock(Message.class);
        when(message.toString()).thenReturn("seat:1");
    }

    @Test
    public void testOnMessage_ValidKey() {
        // Given
        Seat seat = mock(Seat.class);
        when(seat.isSold()).thenReturn(true);
        when(seat.getReservation()).thenReturn(null);

        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // When
        redisExpiredListener.onMessage(message, null);

        // Then
        verify(seat).releaseSeat();
        verify(seatRepository).save(any(Seat.class));
    }

    @Test
    public void testOnMessage_InvalidKey() {
        // Given
        when(seatRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> redisExpiredListener.onMessage(message, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid seatId");
    }
}