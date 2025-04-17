package com.startupgame.entity.game;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Mission {
    @Id
    private Long id;
    private String missionName;

    @ManyToOne
    private Sphere sphere;
}
