package com.startupgame.service.auth;

import com.startupgame.dto.auth.AuthResponse;
import com.startupgame.dto.auth.LoginRequest;
import com.startupgame.dto.auth.RegisterRequest;
import com.startupgame.entity.RegisteredUser;
import com.startupgame.exception.UserAlreadyExistsException;
import com.startupgame.repository.RegisteredUserRepository;
import com.startupgame.security.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Data
@RequiredArgsConstructor
public class AuthService {

    private final RegisteredUserRepository registeredUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthResponse registerNewUser(RegisterRequest request) {
        if (registeredUserRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username is already in use");
        }
        if (registeredUserRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(request.getUsername());
        registeredUser.setEmail(request.getEmail());
        registeredUser.setPassword(passwordEncoder.encode(request.getPassword()));
        registeredUserRepository.save(registeredUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, request.getUsername());
    }

    public String refreshAccessToken(String refreshToken) {
        //TODO validate refresh token
        String username = jwtUtil.extractUsername(refreshToken);
        return jwtUtil.generateAccessToken(new User(username, "", Collections.emptyList()));
    }

    public AuthResponse authenticate(LoginRequest request) {
        return null;
    }
}
