package com.startupgame.entity.game;

import com.startupgame.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "game")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Double difficulty;

    @ManyToOne
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToOne
    private Team team;

    @ManyToMany
    @JoinTable(
            name = "game_modifiers",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "modifier_id")
    )
    private Set<Modifier> modifiers = new HashSet<>();

    private UUID mlGameId;

    //TODO override method toString
}
