package com.startupgame.modules.game.service;

import com.startupgame.modules.game.dto.game.response.GameContext;
import com.startupgame.modules.game.entity.*;
import com.startupgame.modules.game.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GameContextLoader {

    private final GameRepository gameRepository;
    private final TurnRepository turnRepository;
    private final ResourcesRepository resourcesRepository;
    private final TeamRepository teamRepository;
    private final SuperEmployeeRepository superEmployeeRepository;

    public GameContext load(Long gameId) {
        log.debug("Loading game context for gameId: {}", gameId);
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameId));
        Turn currentTurn = turnRepository.findTopByGameIdOrderByTurnNumberDesc(gameId)
                .orElseThrow(() -> new EntityNotFoundException("No turns for game " + gameId));
        Resources resources = resourcesRepository.findById(currentTurn.getResources().getId())
                .orElseThrow(() -> new EntityNotFoundException("Resources not found"));
        Team team = teamRepository.findById(game.getTeam().getId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        List<SuperEmployee> cLevels = superEmployeeRepository.findByTeamId(team.getId());
        return new GameContext(game, currentTurn, resources, team, cLevels, game.getMlGameId());
    }
}
