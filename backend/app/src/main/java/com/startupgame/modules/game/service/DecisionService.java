package com.startupgame.modules.game.service;

import com.startupgame.client.MLApiClient;
import com.startupgame.core.exception.GameAlreadyFinishedException;
import com.startupgame.core.util.GameUtil;
import com.startupgame.modules.game.dto.game.request.DecisionRequest;
import com.startupgame.modules.game.dto.game.response.*;
import com.startupgame.modules.game.dto.ml.request.EvaluateDecisionRequest;
import com.startupgame.modules.game.dto.ml.response.EvaluateDecisionResult;
import com.startupgame.modules.game.entity.Resources;
import com.startupgame.modules.game.entity.Turn;
import com.startupgame.modules.game.repository.GameModifierRepository;
import com.startupgame.modules.game.repository.GameRepository;
import com.startupgame.modules.game.repository.ResourcesRepository;
import com.startupgame.modules.game.repository.TurnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static com.startupgame.core.util.GameUtil.clamp;
import static com.startupgame.core.util.GameUtil.getMonthsPassed;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecisionService {

    private final GameUtil gameUtil;
    private final ResourcesRepository resourcesRepository;
    private final GameModifierRepository gameModifierRepository;
    private final GameLifecycleService gameLifecycleService;
    private final GameContextLoader gameContextLoader;
    private final MLApiClient mlApiClient;
    private final GameRepository gameRepository;
    private final TurnRepository turnRepository;
    private final DiceService diceService;

    @Transactional
    public EvaluateDecisionResponse evaluateDecision(Long gameId, DecisionRequest decisionRequest) {
        log.info("Evaluate decision request: {}", gameId);
        GameContext gameContext = gameContextLoader.load(gameId);
        if (gameContext.getGame().getEndTime() != null) {
            throw new GameAlreadyFinishedException("Game has already finished");
        }
        EvaluateDecisionResult mlResponse = callMl(gameContext, decisionRequest.getDecision());
        ResourceDelta rawDelta = calculateDelta(mlResponse.getQuality_score());
        RollWithMultiplier rollWithMultiplier = diceService.doRoll(gameId);
        double multiplier = rollWithMultiplier.getMultiplier();
        applyChanges(gameContext.getResources(), rawDelta, multiplier);
        applyUpkeepCosts(gameContext);
        if (gameLifecycleService.tryFinishEarly(gameContext)) {
            return buildResponse(rawDelta,
                    gameContext.getResources(),
                    mlResponse,
                    rollWithMultiplier.getRollResponse(),
                    multiplier);
        }
        createNextTurn(gameContext, decisionRequest.getDecision(), mlResponse, rollWithMultiplier.getRollResponse());
        //TODO POST on ml
        return buildResponse(rawDelta, gameContext.getResources(), mlResponse, rollWithMultiplier.getRollResponse(), multiplier);
    }

    @jakarta.transaction.Transactional
    public EvaluateDecisionResponse evaluatePresentation(Long gameId, DecisionRequest decisionRequest) {
        log.info("Evaluate presentation request for gameId: {}", gameId);
        GameContext gameContext = gameContextLoader.load(gameId);
        if (gameContext.getGame().getEndTime() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game has already finished");
        }
        EvaluateDecisionResult mlResponse = callMl(gameContext, decisionRequest.getDecision());
        ResourceDelta rawDelta = calculateDelta(mlResponse.getQuality_score());
        applyChanges(gameContext.getResources(), rawDelta, 1.0);
        applyUpkeepCosts(gameContext);
        if (gameLifecycleService.tryFinishEarly(gameContext)) {
            return buildResponse(rawDelta,
                    gameContext.getResources(),
                    mlResponse,
                    null,
                    1.0);
        }
        return buildResponse(rawDelta, gameContext.getResources(), mlResponse, new RollResponse(0, 0, 0, "presentation"), 1.0);
    }

    private void applyChanges(Resources resources, ResourceDelta resourcesDelta, double multiplier) {
        resources.setMotivation(clamp((int) Math.round((resources.getMotivation() + resourcesDelta.getMotivation()) * multiplier)));
        resources.setTechnicReadiness(clamp((int) Math.round((resources.getTechnicReadiness() + resourcesDelta.getTechnicalReadiness()) * multiplier)));
        resources.setProductReadiness(clamp((int) Math.round((resources.getProductReadiness() + resourcesDelta.getProductReadiness()) * multiplier)));
        resources.setMoney(Math.max(0L, Math.round((resources.getMoney() + resourcesDelta.getMoney()) * multiplier)));
        resourcesRepository.save(resources);
    }

    private EvaluateDecisionResult callMl(GameContext gameContext, String decision) {
        log.info("Calling ml for gameId: {}}", gameContext.getGame().getId());
        DeveloperCounts devCnt = gameModifierRepository.findDeveloperCounts(gameContext.getGame().getId());
        List<String> cLevels = gameModifierRepository.findCLevelNames(gameContext.getGame().getId());

        int juniors = Math.toIntExact(devCnt.juniors());
        int middles = Math.toIntExact(devCnt.middles());
        int seniors = Math.toIntExact(devCnt.seniors());
        EvaluateDecisionRequest req = new EvaluateDecisionRequest(
                gameContext.getGame().getMlGameId(),
                gameContext.getResources().getMoney(),
                gameContext.getResources().getTechnicReadiness(),
                gameContext.getResources().getProductReadiness(),
                gameContext.getResources().getMotivation(),
                getMonthsPassed(gameContext.getTurn()),
                juniors,
                middles,
                seniors,
                cLevels,
                decision
        );
        return mlApiClient.evaluateDecision(req);
    }

    private EvaluateDecisionResponse buildResponse(ResourceDelta rawDelta, Resources resources, EvaluateDecisionResult mlResponse, RollResponse roll, double multiplier) {
        ResourceDelta scaledDelta = createScaledDelta(rawDelta, multiplier);
        return new EvaluateDecisionResponse(
                rawDelta.getMotivation(),
                rawDelta.getTechnicalReadiness(),
                rawDelta.getProductReadiness(),
                rawDelta.getMoney(),
                scaledDelta.getMotivation(),
                scaledDelta.getTechnicalReadiness(),
                scaledDelta.getProductReadiness(),
                scaledDelta.getMoney(),
                resources.getMotivation(),
                resources.getTechnicReadiness(),
                resources.getProductReadiness(),
                resources.getMoney(),
                mlResponse.getText_to_player(),
                mlResponse.getQuality_score(),
                roll
        );
    }

    private ResourceDelta createScaledDelta(ResourceDelta rawDelta, double multiplier) {
        return new ResourceDelta(
                (int) Math.round(rawDelta.getMotivation() * multiplier),
                (int) Math.round(rawDelta.getTechnicalReadiness() * multiplier),
                (int) Math.round(rawDelta.getProductReadiness() * multiplier),
                Math.round(rawDelta.getMoney() * multiplier)
        );
    }

    private void applyUpkeepCosts(GameContext ctx) {
        Long upkeep = gameModifierRepository.sumUpkeepCosts(ctx.getGame().getId());
        Resources res = ctx.getResources();
        res.setMoney(Math.max(0L, res.getMoney() - upkeep));
        resourcesRepository.save(res);
    }

    private void createNextTurn(GameContext gameContext, String decision, EvaluateDecisionResult mlResponse, RollResponse roll) {
        if (gameContext.getGame().getEndTime() != null) {
            return;
        }
        int nextNumber = gameContext.getTurn().getTurnNumber() + 1;
        if (nextNumber > 18) {
            return;
        }
        int currentStage = gameContext.getTurn().getStage();
        int nextStage = (nextNumber % 6 == 0)
                ? Math.min(currentStage + 1, 3)
                : currentStage;
        turnRepository.save(Turn.builder()
                .game(gameContext.getGame())
                .turnNumber(nextNumber)
                .stage(nextStage)
                .resources(gameContext.getResources())
                .situation(decision)
                .answer(decision)
                .score(mlResponse.getQuality_score())
                .diceNumber(roll.getDiceTotal())
                .build());
        if (nextNumber == 18) {
            gameContext.getGame().setEndTime(LocalDateTime.now());
            gameRepository.save(gameContext.getGame());
            gameUtil.endGameWithTransaction(gameContext.getGame().getId());
        }
    }

    private ResourceDelta calculateDelta(double score) {
        score = score * 1500.00;
        if (score >= 0.8) {
            return new ResourceDelta(15, 10, 8, 20000);
        }
        if (score >= 0.6) {
            return new ResourceDelta(10, 7, 5, 10000);
        }
        if (score >= 0.4) {
            return new ResourceDelta(0, 0, 0, 0);
        }
        if (score >= 0.2) {
            return new ResourceDelta(-5, -3, -2, -10000);
        }
        return new ResourceDelta(-10, -5, -4, -20000);
    }
}
