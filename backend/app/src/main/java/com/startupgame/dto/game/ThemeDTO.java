package com.startupgame.dto.game;

import lombok.Data;

@Data
public class ThemeDTO {
    private Long id;
    private String name;
    public ThemeDTO(Long id, String name) {}
}
