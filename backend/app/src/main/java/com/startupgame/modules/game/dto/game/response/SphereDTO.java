package com.startupgame.modules.game.dto.game.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SphereDTO {
    private Long id;
    private String name;
    public SphereDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
