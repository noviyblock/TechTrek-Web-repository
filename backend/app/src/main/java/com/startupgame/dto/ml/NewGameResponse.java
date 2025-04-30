package com.startupgame.dto.ml;

import lombok.Data;
import java.util.UUID;

@Data
public class NewGameResponse {
    private UUID game_id;
}