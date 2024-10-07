package com.example.book_your_seat.config.security.jwt;

import com.example.book_your_seat.config.security.auth.CustomUserDetailsService;
import com.example.book_your_seat.user.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.example.book_your_seat.common.util.JwtConst.*;

@Slf4j
@Component
public class SecurityJwtUtil {
    private final SecretKey secretKey;
    private final Integer expirationTime;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String SIGNATURE_ALGORITHM = Jwts.SIG.HS256.key().build().getAlgorithm();


    SecurityJwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.login_expiration_time}") Integer expirationTime, CustomUserDetailsService customUserDetailsService) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM);
        this.expirationTime = expirationTime;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String createJwt(User user) {
        final Instant now = Instant.now();
        final Instant expiredAt = now.plusSeconds(expirationTime);

        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("role", user.getUserRole().getName())
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
            log.error("JWT 처리 중 오류 발생: {}", e.getMessage());
            throw new IllegalStateException(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(EMPTY_JWT);
        }
    }

    public String getEmailByToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    private String getRoleFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);

    }

    public Authentication getAuthentication(String token) {
        String username = getEmailByToken(token);
        String role = getRoleFromToken(token);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, token, List.of(authority));
    }
}
