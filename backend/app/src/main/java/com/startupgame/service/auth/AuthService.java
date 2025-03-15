package com.startupgame.service.auth;

import com.startupgame.dto.auth.AuthResponse;
import com.startupgame.dto.auth.LoginRequest;
import com.startupgame.dto.auth.RegisterRequest;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthService {

    public void registerNewUser(RegisterRequest request) {
    }

    public AuthResponse authenticate(LoginRequest request) {
        return null;
    }
}
