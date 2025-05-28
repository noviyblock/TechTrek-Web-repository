package com.startupgame.modules.game.dto.game.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartGameRequest {
    private Long missionId;
    private String companyName;
}
