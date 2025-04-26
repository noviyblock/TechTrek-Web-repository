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
@Table(name = "mission")
public class Mission {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    private Sphere sphere;
}
