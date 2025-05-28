package com.startupgame.modules.game.dto.ml.response;

import lombok.Data;
import java.util.UUID;

@Data
public class NewGameResponse {
    private UUID game_id;
}