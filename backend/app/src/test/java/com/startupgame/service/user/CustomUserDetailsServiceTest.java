package com.startupgame.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.startupgame.entity.user.User;
import com.startupgame.repository.user.UserRepository;

@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("tester");
        user.setPassword("password");
        user.setEmail("tester@example.com");
    }

    @Test
    void loadUserByUsername_findsByEmail() {
        when(userRepository.findByEmail("tester@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("tester@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("tester");
    }

    @Test
    void loadUserByUsername_findsByUsername() {
        when(userRepository.findByEmail("tester")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("tester")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("tester");

        assertThat(userDetails.getUsername()).isEqualTo("tester");
    }

    @Test
    void loadUserByUsername_notFound_throwsException() {
        when(userRepository.findByEmail("ghost")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}