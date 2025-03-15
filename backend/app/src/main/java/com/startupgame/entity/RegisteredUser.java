package com.startupgame.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "registered_user")
@Data
public class RegisteredUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    //TODO override method toString
}
