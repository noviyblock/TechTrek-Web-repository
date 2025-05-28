package com.startupgame.modules.game.dto.game.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@AllArgsConstructor
@Data
public class StaffsDTO {
    private Long juniors;
    private Long middles;
    private Long seniors;
    private List<String> c_levels;
}
