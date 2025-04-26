package com.startupgame.dto.game;

import lombok.Data;

@Data
public class SphereDTO {
    private Long id;
    private String name;
    public SphereDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
