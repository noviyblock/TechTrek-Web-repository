package com.startupgame.service;

import com.startupgame.entity.game.Game;
import com.startupgame.entity.game.Mission;
import com.startupgame.repository.game.GameRepository;
import com.startupgame.repository.game.MissionRepository;
import com.startupgame.service.game.GameService;
import com.startupgame.dto.game.StartGameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private GameService gameService;

    private StartGameRequest startGameRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startGameRequest = new StartGameRequest(1L, "My Company", 123L);
    }

    @Test
    void startGame_shouldCreateNewGame() {
        long missionId = startGameRequest.getMissionId();
        Mission mission = new Mission();  // mock mission
        mission.setId(missionId);

        when(missionRepository.findById(missionId)).thenReturn(java.util.Optional.of(mission));
        when(gameRepository.save(any(Game.class))).thenReturn(new Game()); // mock save

        Game result = gameService.startGame(startGameRequest.getMissionId(), startGameRequest.getCompanyName(), startGameRequest.getUserId());

        assertNotNull(result);
        verify(missionRepository, times(1)).findById(missionId);
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void startGame_shouldThrowExceptionWhenMissionNotFound() {
        when(missionRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            gameService.startGame(startGameRequest.getMissionId(), startGameRequest.getCompanyName(), startGameRequest.getUserId());
        });
    }
}
