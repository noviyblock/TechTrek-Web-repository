package com.startupgame.modules.game.dto.game.response;

import com.startupgame.modules.game.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class GameContext {
    private Game game;
    private Turn turn;
    private Resources resources;
    private Team team;
    private List<SuperEmployee> superEmployees;
    UUID mlUuid;
}
