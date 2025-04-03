package com.startupgame.service.auth;

import com.startupgame.dto.auth.AuthResponse;
import com.startupgame.dto.auth.LoginRequest;
import com.startupgame.dto.auth.RegisterRequest;
import com.startupgame.entity.User;
import com.startupgame.exception.UserAlreadyExistsException;
import com.startupgame.repository.UserRepository;
import com.startupgame.security.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Data
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerNewUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username is already in use");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        //TODO save refresh token to db

        return new AuthResponse(accessToken, refreshToken, request.getUsername());
    }

    public String refreshAccessToken(String refreshToken) {
        //TODO validate refresh token and update db
        String username = jwtUtil.extractUsername(refreshToken);
        return jwtUtil.generateAccessToken(new org.springframework.security.core.userdetails.User(username, "", Collections.emptyList()));
    }

    public AuthResponse authenticate(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        //TODO save to db

        return new AuthResponse(accessToken, refreshToken, userDetails.getUsername());
    }
}
