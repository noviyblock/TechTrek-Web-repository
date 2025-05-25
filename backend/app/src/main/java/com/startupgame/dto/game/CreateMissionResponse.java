package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMissionResponse {
    private Long id;
    private String name;
}
