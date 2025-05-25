package com.startupgame.security;

import com.startupgame.entity.user.User;
import com.startupgame.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String auth) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(auth)
                .or(() -> userRepository.findByUsername(auth))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + auth));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}

