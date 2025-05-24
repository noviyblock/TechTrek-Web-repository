package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResourcesDTO {
    private Long money;
    private Integer motivation;
    private Integer technic_readiness;
    private Integer product_readiness;
    private Integer months_passed;
}

