package com.startupgame.service.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class EmailVerificationToken {
    @Id
    private String token;

    @OneToOne
    private User user;

    private LocalDateTime expiryDate;
}