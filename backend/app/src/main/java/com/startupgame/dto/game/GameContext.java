package com.startupgame.dto.game;

import com.startupgame.entity.game.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GameContext {
    private Game game;
    private Turn turn;
    private Resources resources;
    private Team team;
    private List<SuperEmployee> superEmployees;
    UUID mlUuid;
}
