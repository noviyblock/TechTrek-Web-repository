package com.startupgame.modules.game.service;

import com.startupgame.modules.game.dto.game.response.RollResponse;
import com.startupgame.modules.game.dto.game.response.RollWithMultiplier;
import com.startupgame.modules.game.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class DiceService {

    private final GameRepository gameRepository;
    private final Random random = new Random();

    public RollResponse rollDice(Long gameId) {
        gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameId));

        int d1 = random.nextInt(6) + 1;
        int d2 = random.nextInt(6) + 1;
        int total = d1 + d2;

        String zone;
        if (total <= 4) zone = "critical_fail";
        else if (total <= 6) zone = "fail";
        else if (total <= 9) zone = "neutral";
        else if (total <= 11) zone = "success";
        else zone = "critical_success";

        return new RollResponse(total, d1, d2, zone);
    }

    public RollWithMultiplier doRoll(Long gameId) {
        RollResponse roll = rollDice(gameId);
        int total = roll.getDiceTotal();

        double multiplier = switch (total) {
            case 1 -> 0.70;
            case 2, 3 -> 0.85;
            case 4, 5 -> 0.95;
            case 8, 9 -> 1.05;
            case 10, 11 -> 1.15;
            case 12 -> 1.30;
            default -> 1.00;
        };
        return new RollWithMultiplier(roll, multiplier);
    }
}
