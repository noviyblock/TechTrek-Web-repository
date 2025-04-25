package com.startupgame.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
}
