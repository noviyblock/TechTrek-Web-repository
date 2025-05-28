package com.startupgame.service;

import com.startupgame.modules.auth.dto.response.AuthResponse;
import com.startupgame.modules.auth.dto.request.LoginRequest;
import com.startupgame.modules.auth.dto.request.RegisterRequest;
import com.startupgame.modules.auth.entity.RefreshToken;
import com.startupgame.modules.user.User;
import com.startupgame.core.exception.UserAlreadyExistsException;
import com.startupgame.modules.auth.repository.RefreshTokenRepository;
import com.startupgame.modules.user.UserRepository;
import com.startupgame.security.JwtUtil;
import com.startupgame.modules.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    UserDetailsService userDetailsService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterNewUser_Success() {
        RegisterRequest request = new RegisterRequest("nick", "nick@mail.com", "pass123");

        when(userRepository.existsByUsername("nick")).thenReturn(false);
        when(userRepository.existsByEmail("nick@mail.com")).thenReturn(false);

        when(passwordEncoder.encode("pass123")).thenReturn("ENCODED_PASSWORD");

        when(jwtUtil.generateAccessToken(any())).thenReturn("ACCESS_TOKEN");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("REFRESH_TOKEN");

        AuthResponse response = authService.registerNewUser(request);

        assertNotNull(response);
        assertEquals("ACCESS_TOKEN", response.getAccessToken());
        assertEquals("REFRESH_TOKEN", response.getRefreshToken());
        verify(userRepository).save(any(User.class));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testRegisterNewUser_UsernameExists_ThrowsException() {
        //given
        RegisterRequest request = new RegisterRequest("nick", "nick@mail.com", "pass123");
        when(userRepository.existsByUsername("nick")).thenReturn(true);

        //when / then
        assertThrows(UserAlreadyExistsException.class, () -> authService.registerNewUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRefreshAccessToken_Success() {
        //given
        String existingRefreshToken = "VALID_REFRESH_TOKEN";
        RefreshToken entity = RefreshToken.builder()
                .tokenValue(existingRefreshToken)
                .build();

        when(refreshTokenRepository.findByTokenValue(existingRefreshToken))
                .thenReturn(Optional.of(entity));

        when(jwtUtil.extractUsername(existingRefreshToken)).thenReturn("nick");
        when(userDetailsService.loadUserByUsername("nick"))
                .thenReturn(org.springframework.security.core.userdetails.User
                        .withUsername("nick")
                        .password("ENCODED")
                        .roles("USER")
                        .build());
        when(jwtUtil.validateToken(eq(existingRefreshToken), any())).thenReturn(true);
        when(jwtUtil.generateAccessToken(any())).thenReturn("NEW_ACCESS_TOKEN");

        //when
        String newAccessToken = String.valueOf(authService.refreshAccessToken(existingRefreshToken));
        //then
        assertEquals("NEW_ACCESS_TOKEN", newAccessToken);
        verify(refreshTokenRepository, never()).delete(entity);
    }

    @Test
    void testRefreshAccessToken_InvalidToken_Throws() {
        //given
        String invalidRefreshToken = "INVALID";
        when(refreshTokenRepository.findByTokenValue(invalidRefreshToken))
                .thenReturn(Optional.empty());

        //when/then
        assertThrows(RuntimeException.class, () -> authService.refreshAccessToken(invalidRefreshToken));
    }

    @Test
    void testAuthenticate_Success() {
        // given
        LoginRequest loginRequest = new LoginRequest("nick@mail.com", "pass123");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("nick");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtil.generateAccessToken(any())).thenReturn("ACCESS_TOKEN");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("REFRESH_TOKEN");

        User user = new User();
        user.setUsername("nick");
        when(userRepository.findByUsername("nick")).thenReturn(Optional.of(user));

        //when
        AuthResponse response = authService.authenticate(loginRequest);

        //then
        assertNotNull(response);
        assertEquals("ACCESS_TOKEN", response.getAccessToken());
        assertEquals("REFRESH_TOKEN", response.getRefreshToken());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

}