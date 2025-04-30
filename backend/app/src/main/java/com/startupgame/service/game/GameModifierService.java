package com.startupgame.service.game;

import com.startupgame.entity.game.Game;
import com.startupgame.entity.game.GameModifier;
import com.startupgame.entity.game.Modifier;
import com.startupgame.repository.game.GameModifierRepository;
import com.startupgame.repository.game.ModifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameModifierService {

    private final ModifierRepository modifierRepository;
    private final GameModifierRepository gameModifierRepository;

    public void initializeDefaultModifiersForGame(Game game) {
        Modifier ceoModifier = modifierRepository.findByName("CEO")
                .orElseThrow(() -> new IllegalStateException("Modifier 'CEO' not found"));
        Modifier juniorDevModifier = modifierRepository.findByName("JUNIOR_DEV")
                .orElseThrow(() -> new IllegalStateException("Modifier 'JUNIOR_DEV' not found"));
        Modifier coworkingModifier = modifierRepository.findByName("COWORKING")
                .orElseThrow(() -> new IllegalStateException("Modifier 'COWORKING' not found"));

        Map<Modifier, Integer> defaults = Map.of(
                ceoModifier, 1,
                juniorDevModifier, 2,
                coworkingModifier, 1
        );

        for (var entry : defaults.entrySet()) {
            Modifier modifier = entry.getKey();
            int quantityIssued = entry.getValue();

            GameModifier gameModifier = GameModifier.builder()
                    .game(game)
                    .modifier(modifier)
                    .usageCount(0)
                    .quantity(quantityIssued)
                    .active(true)
                    .build();

            gameModifierRepository.save(gameModifier);
        }
    }
}
