package com.example.book_your_seat.service.seat;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.redis.RedisExpiredListener;
import com.example.book_your_seat.seat.redis.SeatRedisService;
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

public class RedisMessageTest extends IntegralTestSupport {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private RedisMessageListenerContainer listenerContainer;

    @Mock
    private SeatRedisService seatRedisService;

    @InjectMocks
    private RedisExpiredListener redisExpiredListener;

    private Message message;

    @BeforeEach
    public void setUp() {
        // Message 객체 목 설정
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
        verify(seatRedisService).deleteCache(anyLong());
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