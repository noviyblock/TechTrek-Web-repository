package com.startupgame.service;

import com.startupgame.dto.game.GameStateDTO;
import com.startupgame.entity.game.*;
import com.startupgame.entity.user.User;
import com.startupgame.repository.game.*;
import com.startupgame.repository.user.UserRepository;
import com.startupgame.service.game.GameService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private ResourcesRepository resourcesRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TurnRepository turnRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).username("tester").build();
        Mission mission = Mission.builder().id(42L).name("Demo mission").build();

        lenient().when(userRepository.findByUsername("tester")).thenReturn(Optional.of(user));
        lenient().when(missionRepository.findById(42L)).thenReturn(Optional.of(mission));

        lenient().when(resourcesRepository.save(any(Resources.class)))
                .thenAnswer(i -> i.getArgument(0));
        lenient().when(teamRepository.save(any(Team.class)))
                .thenAnswer(i -> i.getArgument(0));
        lenient().when(gameRepository.save(any(Game.class)))
                .thenAnswer(i -> i.getArgument(0));
        lenient().when(turnRepository.save(any(Turn.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void startGame_happyPath_returnsFilledDTO() {
        GameStateDTO dto = gameService.startGame(42L, "Trace", "tester");

        assertThat(dto).isNotNull();
        assertThat(dto.getMissionId()).isEqualTo(42L);
        assertThat(dto.getCompanyName()).isEqualTo("Trace");
        assertThat(dto.getTurnNumber()).isEqualTo(1);
        assertThat(dto.getMoney()).isEqualTo(100000L);

        verify(gameRepository).save(any(Game.class));
        verify(resourcesRepository).save(any(Resources.class));
        verify(teamRepository).save(any(Team.class));
        verify(turnRepository).save(any(Turn.class));
    }

    @Test
    void startGame_userNotFound_throws() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.startGame(42L, "Trace", "ghost"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void startGame_missionNotFound_throws() {
        when(missionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.startGame(99L, "Trace", "tester"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Mission not found");
    }
}
