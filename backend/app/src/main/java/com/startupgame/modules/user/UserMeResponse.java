package com.startupgame.modules.user;

import com.startupgame.modules.game.dto.game.response.GameShortInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserMeResponse {
    private String username;
    private String email;
    private List<GameShortInfoDTO> games;
}
