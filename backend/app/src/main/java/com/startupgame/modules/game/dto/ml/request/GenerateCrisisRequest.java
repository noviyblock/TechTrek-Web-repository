package com.startupgame.modules.game.dto.ml.request;

import com.startupgame.modules.game.dto.game.response.ResourcesDTO;
import com.startupgame.modules.game.dto.game.response.StaffsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GenerateCrisisRequest {
    private ResourcesDTO res;
    private StaffsDTO staffs;
    private UUID game_id;
}