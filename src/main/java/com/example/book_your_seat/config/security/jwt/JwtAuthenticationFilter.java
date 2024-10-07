package com.example.book_your_seat.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.book_your_seat.common.util.JwtConst.UNSUPPORTED_JWT;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_TYPE = "Bearer ";
    private final SecurityJwtUtil securityJwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        //token 이 없으면 anonymous User
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        validateJwtAuthorizationType(authorization);
        String jwt = authorization.substring(TOKEN_TYPE.length());

        //token 검증이 완료된 경우에만 authentication을 부여
        if (securityJwtUtil.validateToken(jwt)) {
            log.info("jwt :" + jwt);
            Authentication authentication = securityJwtUtil.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private void validateJwtAuthorizationType(String authorization) {
        if (!authorization.startsWith(TOKEN_TYPE))
            throw new IllegalArgumentException(UNSUPPORTED_JWT);
    }
}
