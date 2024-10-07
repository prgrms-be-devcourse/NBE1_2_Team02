package com.example.book_your_seat.config.security.auth;

import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.user.UserConst.INVALID_EMAIL;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username이 email
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_EMAIL));

        return new CustomUserDetails(user);
    }
}
