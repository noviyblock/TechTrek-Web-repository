package com.startupgame.core.util;

import com.startupgame.modules.game.dto.game.response.GameContext;
import com.startupgame.modules.game.entity.*;
import com.startupgame.modules.game.service.GameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;

import java.util.List;

@Slf4j
public class GameUtil {
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

    public static void endGameWithTransaction(Long gameId) {
        GameService self = (GameService) AopContext.currentProxy();
        self.finalizeGame(gameId);
    }
}
