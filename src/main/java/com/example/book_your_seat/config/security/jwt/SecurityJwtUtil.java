package com.example.book_your_seat.config.security.jwt;

import com.example.book_your_seat.common.util.JwtUtil;
import com.example.book_your_seat.config.security.auth.CustomUserDetailsService;
import com.example.book_your_seat.user.domain.User;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SecurityJwtUtil extends JwtUtil {

    private final Integer expirationTime;
    private final CustomUserDetailsService customUserDetailsService;

    SecurityJwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.login_expiration_time}") Integer expirationTime, CustomUserDetailsService customUserDetailsService) {
        super(secretKey);
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
