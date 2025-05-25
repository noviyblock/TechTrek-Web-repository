package com.startupgame.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION = 15 * 60 * 1000;
    private static final long WINDOW = 60 * 1000;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if ("/api/auth/login".equals(path)) {
            String ip = request.getRemoteAddr();
            Attempt attempt = attempts.computeIfAbsent(ip, k -> new Attempt());

            long now = System.currentTimeMillis();

            if (now - attempt.firstAttemptTime > WINDOW) {
                attempt.reset(now);
            }

            if (attempt.blockedUntil > now) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many login attempts. Try again later.");
                return;
            }

            attempt.count++;

            if (attempt.count > MAX_ATTEMPTS) {
                attempt.blockedUntil = now + BLOCK_DURATION;
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many login attempts. You are temporarily blocked.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static class Attempt {
        int count = 0;
        long firstAttemptTime = System.currentTimeMillis();
        long blockedUntil = 0;

        void reset(long now) {
            count = 1;
            firstAttemptTime = now;
            blockedUntil = 0;
        }
    }
}
