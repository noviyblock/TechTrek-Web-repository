package com.startupgame.core.util;

import com.startupgame.modules.game.dto.game.response.GameContext;
import com.startupgame.modules.game.entity.*;
import com.startupgame.modules.game.service.GameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameUtil {

    private final GameService gameService;

    public static int getMonthsPassed(Turn turn) {
        int tn = turn.getTurnNumber();
        int s1 = Math.min(tn, 6);
        int s2 = Math.min(Math.max(tn - 6, 0), 6);
        int s3 = Math.min(Math.max(tn - 12, 0), 6);
        return s1 + s2 * 3 + s3 * 6;
    }

    public static int clamp(int val) {
        return Math.min(100, Math.max(0, val));
    }

    public void endGameWithTransaction(Long gameId) {
        gameService.finalizeGame(gameId);
    }
}
