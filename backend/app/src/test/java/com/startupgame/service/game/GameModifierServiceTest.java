package com.startupgame.service.game;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.startupgame.entity.game.Game;
import com.startupgame.entity.game.GameModifier;
import com.startupgame.entity.game.Modifier;
import com.startupgame.repository.game.GameModifierRepository;
import com.startupgame.repository.game.ModifierRepository;

@ExtendWith(SpringExtension.class)
class GameModifierServiceTest {

    @InjectMocks
    private GameModifierService gameModifierService;

    @Mock
    private ModifierRepository modifierRepository;

    @Mock
    private GameModifierRepository gameModifierRepository;

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setId(1L);
    }

    @Test
    void initializeDefaultModifiersForGame_createsThreeModifiers() {
        Modifier ceo = new Modifier();
        ceo.setName("CEO");

        Modifier juniorDev = new Modifier();
        juniorDev.setName("JUNIOR_DEV");

        Modifier coworking = new Modifier();
        coworking.setName("COWORKING");

        when(modifierRepository.findByName("CEO")).thenReturn(java.util.Optional.of(ceo));
        when(modifierRepository.findByName("JUNIOR_DEV")).thenReturn(java.util.Optional.of(juniorDev));
        when(modifierRepository.findByName("COWORKING")).thenReturn(java.util.Optional.of(coworking));

        gameModifierService.initializeDefaultModifiersForGame(game);

        verify(gameModifierRepository, times(3)).save(any(GameModifier.class));
    }
}
