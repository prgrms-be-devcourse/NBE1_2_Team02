package com.example.book_your_seat.common.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static com.example.book_your_seat.common.util.JwtConst.*;

@Slf4j
@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Integer expirationTime;
    private static final String SIGNATURE_ALGORITHM = Jwts.SIG.HS256.key().build().getAlgorithm();

    JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration_time}") Integer expirationTime) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM);
        this.expirationTime = expirationTime;
    }

    public String createJwt(Long userId) {
        final Instant now = Instant.now();
        final Instant expiredAt = now.plusSeconds(expirationTime);

        return Jwts.builder()
                .claim("userId", userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiredAt))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new IllegalStateException(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException(EXPIRED_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new IllegalStateException(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(EMPTY_JWT);
        }
    }

    public Long getUserIdByToken(String token) {
        String userId = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);

        return Long.parseLong(userId);
    }
}
