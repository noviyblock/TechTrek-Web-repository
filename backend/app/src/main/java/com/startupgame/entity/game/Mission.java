package com.startupgame.entity.game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mission {
    @Id
    private Long id;
    private String missionName;

    @ManyToOne
    private Sphere sphere;
}
