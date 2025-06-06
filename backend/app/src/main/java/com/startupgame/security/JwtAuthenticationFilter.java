package com.startupgame.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.startupgame.modules.user.User;
import com.startupgame.modules.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String role = jwtUtil.extractRole(jwt);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                if ("ROLE_GUEST".equals(role)) {
                    if (jwtUtil.validateToken(jwt)) {
                        MDC.put("username", username);

                        UsernamePasswordAuthenticationToken guestAuth =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);
                        guestAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(guestAuth);
                    }
                } else {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        User user = userRepository.findByUsername(userDetails.getUsername())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                        MDC.put("userId", String.valueOf(user.getId()));
                        MDC.put("username", user.getUsername());

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Token expired");
            errorDetails.put("message", e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorDetails);

            response.getWriter().write(jsonResponse);
        } finally {
            MDC.clear();
        }
    }
}
