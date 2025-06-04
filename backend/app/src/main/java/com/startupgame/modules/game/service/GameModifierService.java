package com.startupgame.modules.game.service;

import com.startupgame.modules.game.entity.Game;
import com.startupgame.modules.game.entity.GameModifier;
import com.startupgame.modules.game.entity.Modifier;
import com.startupgame.modules.game.repository.GameModifierRepository;
import com.startupgame.modules.game.repository.ModifierRepository;
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
