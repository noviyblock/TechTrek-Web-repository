package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long gameId;
    private Long modifierId;
    private long remainingMoney;
    private List<String> ownedModifiers;
}