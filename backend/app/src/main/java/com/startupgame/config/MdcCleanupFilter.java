package com.startupgame.config;

import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import org.slf4j.MDC;

import java.io.IOException;

@Component
public class MdcCleanupFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
