package com.startupgame.dto.user;

import com.startupgame.dto.game.GameShortInfoDTO;
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
