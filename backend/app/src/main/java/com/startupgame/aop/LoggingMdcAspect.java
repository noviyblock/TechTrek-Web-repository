package com.startupgame.aop;

import com.startupgame.modules.user.User;
import com.startupgame.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingMdcAspect {

    private final UserRepository userRepository;

    @Before("execution(* com.startupgame.service..*(..))")
    public void beforeServiceCall() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
            MDC.put("username", username);
            MDC.put("userId", String.valueOf(user.getId()));
        } else {
            MDC.put("username", "anonymous");
        }
    }
}
