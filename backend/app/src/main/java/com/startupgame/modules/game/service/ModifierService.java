package com.startupgame.modules.game.service;

import com.startupgame.core.exception.InsufficientFundsException;
import com.startupgame.modules.game.dto.game.response.ModifierResponse;
import com.startupgame.modules.game.dto.game.response.PurchaseResponse;
import com.startupgame.modules.game.entity.*;
import com.startupgame.modules.game.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModifierService {
    private final GameModifierRepository gameModifierRepository;
    private final GameRepository gameRepository;
    private final ModifierRepository modifierRepository;
    private final ResourcesRepository resourcesRepository;
    private final TurnRepository turnRepository;

    /**
     * Выполняет покупку модификатора в рамках конкретной игры.
     * <p>
     * Метод:
     * <ul>
     *   <li>Проверяет, что игра с указанным gameId существует.</li>
     *   <li>Проверяет, что модификатор с указанным modifierId существует.</li>
     *   <li>Получает последний ход и извлекает из него текущие ресурсы.</li>
     *   <li>Проверяет, что у игрока достаточно денег для покупки.</li>
     *   <li>Списывает стоимость покупки из баланса и сохраняет Resources.</li>
     *   <li>Создаёт запись в таблице game_modifiers.</li>
     *   <li>Возвращает результат покупки с оставшимся балансом и списком уже купленных модификаторов.</li>
     * </ul>
     *
     * @param gameId     идентификатор игры, в которой проводится покупка
     * @param modifierId идентификатор модификатора для покупки
     * @return {@link PurchaseResponse}, содержащий:
     * <ul>
     *   <li>gameId – идентификатор игры;</li>
     *   <li>modifierId – идентификатор купленного модификатора;</li>
     *   <li>remainingMoney – остаток средств после списания;</li>
     *   <li>ownedModifiers – список названий всех купленных модификаторов.</li>
     * </ul>
     * @throws EntityNotFoundException    если игра, модификатор или последний ход не найдены
     * @throws InsufficientFundsException если средств на балансе меньше стоимости покупки
     */
    @Transactional
    public PurchaseResponse purchaseModifier(Long gameId, Long modifierId) {
        log.info("Purchase modifier for gameId: {}, modifierId: {}", gameId, modifierId);
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Game with id=" + gameId + " not found"));

        Modifier modifier = modifierRepository.findById(modifierId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modifier with id=" + modifierId + " not found"));

        Turn currentTurn = turnRepository
                .findTopByGameIdOrderByTurnNumberDesc(gameId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No turns found for game id=" + gameId));

        GameModifier existing = gameModifierRepository.findByGameIdAndModifierId(gameId, modifierId)
                .orElse(null);

        Resources resources = currentTurn.getResources();

        boolean isDeveloper = modifier.getModifierType() == ModifierType.JUNIOR
                || modifier.getModifierType() == ModifierType.MIDDLE
                || modifier.getModifierType() == ModifierType.SENIOR;

        boolean isOffice = modifier.getModifierType() == ModifierType.OFFICE;


        if (existing != null && !isDeveloper) {
            throw new IllegalStateException("Modifier of type " + modifier.getModifierType() + " can only be purchased once");
        }

        if (currentTurn.getStage() < modifier.getStageAllowed()) {
            throw new IllegalStateException("Modifier of type " + modifier.getModifierType() + " can only be purchased on stage " + modifier.getStageAllowed());
        }

        Long cost = modifier.getPurchaseCost();
        if (resources.getMoney() < cost) {
            throw new InsufficientFundsException(
                    String.format("Not enough funds: have %d but need %d",
                            resources.getMoney(), cost));
        }

        resources.setMoney(resources.getMoney() - cost);
        resourcesRepository.save(resources);

        if (isOffice) {
            gameModifierRepository.findActiveOffice(gameId)
                    .flatMap(oldMod ->
                            gameModifierRepository.findByGameIdAndModifierId(gameId, oldMod.getId())
                    )
                    .ifPresent(oldGameMod -> {
                        oldGameMod.setActive(false);
                        gameModifierRepository.delete(oldGameMod);
                    });
        }

        GameModifier savedModifier;
        if (existing == null) {
            savedModifier = new GameModifier(game, modifier);
            savedModifier.setQuantity(1);
        } else {
            existing.setQuantity(existing.getQuantity() + 1);
            savedModifier = existing;
        }
        gameModifierRepository.save(savedModifier);

        List<String> owned = gameModifierRepository.findByGameId(gameId)
                .stream()
                .map(gm -> gm.getModifier().getName())
                .collect(Collectors.toList());

        return new PurchaseResponse(
                gameId,
                modifierId,
                resources.getMoney(),
                owned,
                savedModifier.getQuantity()
        );
    }

    public List<ModifierResponse> getAllModifiers(Long gameId) {
        log.info("Getting modifiers for gameId: {}", gameId);
        List<Long> ownedIds = gameModifierRepository
                .findByGameId(gameId)
                .stream()
                .map(item -> item.getModifier().getId())
                .toList();
        return modifierRepository.findAll()
                .stream()
                .map(mod -> ModifierResponse.builder()
                        .id(mod.getId())
                        .name(mod.getName())
                        .type(mod.getModifierType())
                        .purchaseCost(mod.getPurchaseCost())
                        .upkeepCost(mod.getUpkeepCost())
                        .stageAllowed(mod.getStageAllowed())
                        .owned(ownedIds.contains(mod.getId()))
                        .build())
                .toList();
    }
}
