package com.startupgame.modules.game.dto.game.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MissionResponse {
    @JsonProperty("first")
    private String firstMission;
    @JsonProperty("second")
    private String secondMission;
    @JsonProperty("third")
    private String thirdMission;
}
