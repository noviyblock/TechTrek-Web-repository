package com.startupgame.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserGameId implements Serializable {
    private Long userId;
    private Long gameId;
}
