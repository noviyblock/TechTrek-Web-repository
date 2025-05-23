package com.startupgame.dto.ml;

import lombok.Data;

@Data
public class NewGameRequest {
    private String sphere;
    private String mission;
    private String startup_name;
    private Long money;
    private Integer technicReadiness;
    private Integer productReadiness;
    private Integer motivation;
}
