package com.startupgame.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.startupgame.dto.game.ResourcesDTO;
import com.startupgame.dto.game.StaffsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GenerateCrisisRequest {
    private ResourcesDTO res;
    private StaffsDTO staffs;
    private UUID game_id;
}