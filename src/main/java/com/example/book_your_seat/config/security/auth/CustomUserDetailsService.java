package com.example.book_your_seat.config.security.auth;

import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.user.UserConst.INVALID_EMAIL;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username이 email
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_EMAIL));

        return new CustomUserDetails(user);
    }
}
