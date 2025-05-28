package com.startupgame.service.auth;

import java.security.SecureRandom;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final SecureRandom RND = new SecureRandom();
    private final CacheManager cacheManager;

    /**
     * Генерирует OTP код, сохраняет его в кэше и возвращает.
     * Вызывается при регистрации пользователя.
     */
    public String generateOtp(String email) {
        String code = String.format("%05d", RND.nextInt(100_000));
        cacheManager.getCache("otp").put(email, code);
        return code;
    }

    /**
     * Проверяет правильность OTP-кода и удаляет его из кэша при успешной верификации.
     */
    public boolean validateOtp(String email, String code) {
        Cache cacheOtp = cacheManager.getCache("otp");
        if (cacheOtp == null) {
            return false;
        }
        String cached = cacheOtp.get(email, String.class);
        if (code.equals(cached)) {
            cacheOtp.evict(email);
            return true;
        }
        return false;
    }
}


