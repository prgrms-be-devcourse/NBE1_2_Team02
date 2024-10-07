package com.example.book_your_seat.config.security;

import com.example.book_your_seat.config.security.auth.CustomUserDetailsService;
import com.example.book_your_seat.config.security.jwt.JwtAuthenticationFilter;
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil;
import com.example.book_your_seat.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final SecurityJwtUtil securityJwtUtil;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);
        //Form login disable
        http
                .formLogin(AbstractHttpConfigurer::disable);
        //http basic disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //세션 사용하지 않음 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //경로별 인가
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/api-docs/**").permitAll()

                        .requestMatchers("/api/v1/users","/api/v1/users/login").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.getName())
                        .anyRequest().authenticated());
        http
                .addFilterBefore(new JwtAuthenticationFilter(securityJwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //인증 관리자 관련 성정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject
                .userDetailsService(customUserDetailsService) //사용자 정보 조회
                .passwordEncoder(bCryptPasswordEncoder);

        return sharedObject.build();
    }
}