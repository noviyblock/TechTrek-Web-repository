package com.startupgame.dto.ml;

import lombok.Data;

@Data
public class NewGameRequest {
    private Long money;
    private Integer technicReadiness;
    private Integer productReadiness;
    private Integer motivation;
}
