package com.startupgame.service.game;

import com.startupgame.client.MLApiClient;
import com.startupgame.dto.game.*;
import com.startupgame.dto.ml.*;
import com.startupgame.entity.game.*;
import com.startupgame.entity.user.User;
import com.startupgame.exception.GameAlreadyFinishedException;
import com.startupgame.exception.InsufficientFundsException;
import com.startupgame.exception.MlServiceUnavailableException;
import com.startupgame.repository.game.*;
import com.startupgame.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final SphereRepository themeRepository;
    private final GameRepository gameRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ResourcesRepository resourcesRepository;
    private final TeamRepository teamRepository;
    private final TurnRepository turnRepository;
    private final SuperEmployeeRepository superEmployeeRepository;
    private final ModifierRepository modifierRepository;
    private final GameModifierRepository gameModifierRepository;
    private final MLApiClient mlApiClient;
    private final GameModifierService gameModifierService;
    private final YandexTranslateService yandexTranslateService;
    private final GameFinalScoreRepository gameFinalScoreRepository;
    private final Random random = new Random();

    public List<SphereDTO> getSpheres() {
        List<Sphere> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> new SphereDTO(theme.getId(), theme.getName()))
                .collect(Collectors.toList());
    }


    /**
     * Инициализирует новую игру для указанного пользователя.
     * <p>
     * <ul>
     *     <li>Проверяет наличие пользователя по имени</li>
     *     <li>Проверяет наличие миссии по ID</li>
     *     <li>Создаёт и сохраняет начальные ресурсы и команду</li>
     *     <li>Создаёт объект игры и сохраняет его в базу данных</li>
     *     <li>Создаёт первый ход (turn) со стартовой ситуацией</li>
     * </ul>
     * <p>
     * Метод помечен {@code @Transactional}, для атомарности всей операции.
     * Если произойдет ошибка на любом этапе, все изменения в базе данных будут откатаны.
     *
     * @param missionId   идентификатор миссии
     * @param companyName название компании
     * @param username    имя пользователя
     * @return объект {@link Game}, созданная игра
     * @throws EntityNotFoundException  если пользователь с указанным именем не найден
     * @throws IllegalArgumentException если миссия с таким id отсутствует
     */
    @Transactional
    public GameStateDTO startGame(Long missionId, String companyName, String username) {
        log.info("Starting game with missionId: {}", missionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));

        NewGameRequest newGameRequest = new NewGameRequest();
        newGameRequest.setSphere(mission.getSphere().getName());
        newGameRequest.setMission(mission.getName());
        newGameRequest.setStartup_name(companyName);
        newGameRequest.setMoney(100000L);
        newGameRequest.setTechnicReadiness(0);
        newGameRequest.setProductReadiness(0);
        newGameRequest.setMotivation(50);
        NewGameResponse newGameResponse;
        try {
            newGameResponse = mlApiClient.createGame(newGameRequest);
        } catch (Exception ex) {
            log.error("ML service createGame failed for missionId={}, user='{}'", missionId, username, ex);
            throw new MlServiceUnavailableException(
                    "ML service createGame failed", ex
            );
        }


        Resources resources = resourcesRepository.save(Resources.builder()
                .money(100000L)
                .motivation(50)
                .productReadiness(0)
                .technicReadiness(0)
                .numberOfOffices(0)
                .build());

        Team team = teamRepository.save(Team.builder()
                .juniorAmount(0)
                .middleAmount(0)
                .seniorAmount(0)
                .build());

        Game game = gameRepository.save(Game.builder()
                .companyName(companyName)
                .difficulty(1.0)
                .mission(mission)
                .user(user)
                .team(team)
                .startTime(LocalDateTime.now())
                .endTime(null)
                .mlGameId(newGameResponse.getGame_id())
                .build());

        gameModifierService.initializeDefaultModifiersForGame(game);

        Turn firstTurn = turnRepository.save(Turn.builder()
                .game(game)
                .turnNumber(0)
                .stage(1)
                .resources(resources)
                .situation("Начало игры")
                .build());

        return buildGameStateDTO(game, firstTurn, resources);
    }

    @Transactional
    public GameStateDTO getCurrentState(Long gameId) {
        log.info("Getting current state for gameId: {}", gameId);
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Game with id=%d not found", gameId)));

        Turn currentTurn = turnRepository
                .findTopByGameIdOrderByTurnNumberDesc(gameId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No turns found for game with id=%d", gameId)));

        Resources currentResources = resourcesRepository.findById(currentTurn.getResources().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Resources for turn %d not found", currentTurn.getTurnNumber())));

        return buildGameStateDTO(game, currentTurn, currentResources);
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

    @Transactional
    public EvaluateDecisionResponse evaluateDecision(Long gameId, DecisionRequest decisionRequest) {
        log.info("Evaluate decision request: {}", gameId);
        GameContext gameContext = loadGameContext(gameId);
        if (gameContext.getGame().getEndTime() != null) {
            throw new GameAlreadyFinishedException("Game has already finished");
        }
        EvaluateDecisionResult mlResponse = callMl(gameContext, decisionRequest.getDecision());
        ResourceDelta rawDelta = calculateDelta(mlResponse.getQuality_score());
        RollWithMultiplier rollWithMultiplier = doRoll(gameId);
        double multiplier = rollWithMultiplier.getMultiplier();
        applyChanges(gameContext.getResources(), rawDelta, multiplier);
        if (tryFinishEarly(gameContext)) {
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

    @Transactional
    public EvaluateDecisionResponse evaluatePresentation(Long gameId, DecisionRequest decisionRequest) {
        log.info("Evaluate presentation request for gameId: {}", gameId);
        GameContext gameContext = loadGameContext(gameId);
        if (gameContext.getGame().getEndTime() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game has already finished");
        }
        EvaluateDecisionResult mlResponse = callMl(gameContext, decisionRequest.getDecision());
        ResourceDelta rawDelta = calculateDelta(mlResponse.getQuality_score());
        applyChanges(gameContext.getResources(), rawDelta, 1.0);
        if (tryFinishEarly(gameContext)) {
            return buildResponse(rawDelta,
                    gameContext.getResources(),
                    mlResponse,
                    null,
                    1.0);
        }
        return buildResponse(rawDelta, gameContext.getResources(), mlResponse, new RollResponse(0, 0, 0, "presentation"), 1.0);
    }

    public CrisisResponse generateCrisis(Long gameId) {
        log.info("Generate crisis for game id: {}", gameId);

        GameContext gameContext = loadGameContext(gameId);
        Game game = gameContext.getGame();

        DeveloperCounts devCnt = gameModifierRepository.findDeveloperCounts(game.getId());
        List<String> cLevels = gameModifierRepository.findCLevelNames(game.getId());

        ResourcesDTO res = new ResourcesDTO(
                gameContext.getResources().getMoney(),
                gameContext.getResources().getTechnicReadiness(),
                gameContext.getResources().getProductReadiness(),
                gameContext.getResources().getMotivation(),
                getMonthsPassed(gameContext.getTurn())
        );

        StaffsDTO staffs = new StaffsDTO(
                devCnt.juniors(),
                devCnt.middles(),
                devCnt.seniors(),
                cLevels
        );

        GenerateCrisisRequest generateCrisisRequest = new GenerateCrisisRequest(
                res,
                staffs,
                game.getMlGameId()
        );

        log.debug("Generate crisis request: {}", generateCrisisRequest);

        CrisisResponse response = mlApiClient.generateCrisis(generateCrisisRequest);

        if (response.getDescription() != null && !response.getDescription().isBlank()) {
            String translated = yandexTranslateService.translateText(response.getDescription(), "ru");
            response.setDescription(translated);
        }

        return response;
    }

    @Transactional
    public void finalizeGame(Long gameId) {
        log.info("Finalizing game {}", gameId);

        GameContext ctx = loadGameContext(gameId);
        Game game = ctx.getGame();

        if (game.getEndTime() == null) {
            game.setEndTime(LocalDateTime.now());
            gameRepository.save(game);
        }

        int minMotivation = turnRepository.findAllByGameId(gameId).stream()
                .mapToInt(t -> resourcesRepository.findById(t.getResources().getId())
                        .orElseThrow()
                        .getMotivation())
                .min()
                .orElse(ctx.getResources().getMotivation());

        boolean motivationNeverBelow50 = minMotivation >= 50;

        int monthsPassed = getMonthsPassed(ctx.getTurn());

        FinalScoreDTO score = FinalScoreCalculator.calculate(
                ctx.getResources().getMoney(),
                ctx.getResources().getTechnicReadiness(),
                ctx.getResources().getProductReadiness(),
                ctx.getResources().getMotivation(),
                monthsPassed,
                false, //TODO
                motivationNeverBelow50
        );

        GameFinalScore gameFinalScore = getGameFinalScore(game, score);
        gameFinalScoreRepository.save(gameFinalScore);

        game.setScore(score.totalScore());
        gameRepository.save(game);

    }

    private static GameFinalScore getGameFinalScore(Game game, FinalScoreDTO score) {
        GameFinalScore gameFinalScore = new GameFinalScore();
        gameFinalScore.setGame(game);
        gameFinalScore.setMoneyScore(score.moneyScore());
        gameFinalScore.setTechScore(score.techScore());
        gameFinalScore.setProductScore(score.productScore());
        gameFinalScore.setMotivationScore(score.motivationScore());
        gameFinalScore.setTimeScore(score.timeScore());
        gameFinalScore.setBonusScore(score.bonusScore());
        gameFinalScore.setTotalScore(score.totalScore());
        return gameFinalScore;
    }

    private boolean tryFinishEarly(GameContext ctx) {
        Resources resources = ctx.getResources();
        if (resources.getTechnicReadiness() >= 100 && resources.getProductReadiness() >= 100) {
            if (ctx.getGame().getEndTime() == null) {
                ctx.getGame().setEndTime(LocalDateTime.now());
                gameRepository.save(ctx.getGame());
            }
            endGameWithTransaction(ctx.getGame().getId());
            return true;
        }
        return false;
    }


    private GameStateDTO buildGameStateDTO(Game game, Turn turn, Resources resources) {
        DeveloperCounts devCnt = gameModifierRepository.findDeveloperCounts(game.getId());
        Modifier office = gameModifierRepository.findActiveOffice(game.getId()).orElse(null);
        List<String> cLevels = gameModifierRepository.findCLevelNames(game.getId());

        FinalScoreDTO finalScore = gameFinalScoreRepository.findByGameId(game.getId())
                .map(gfs -> FinalScoreDTO.builder()
                        .moneyScore(gfs.getMoneyScore())
                        .techScore(gfs.getTechScore())
                        .productScore(gfs.getProductScore())
                        .motivationScore(gfs.getMotivationScore())
                        .timeScore(gfs.getTimeScore())
                        .bonusScore(gfs.getBonusScore())
                        .totalScore(gfs.getTotalScore())
                        .build())
                .orElse(null);

        assert office != null;
        return GameStateDTO.builder()
                .gameId(game.getId())
                .companyName(game.getCompanyName())
                .stage(turn.getStage())
                .turnNumber(turn.getTurnNumber())
                .monthsPassed(getMonthsPassed(turn))
                .money(resources.getMoney())
                .technicReadiness(resources.getTechnicReadiness())
                .productReadiness(resources.getProductReadiness())
                .motivation(resources.getMotivation())
                .juniors(devCnt.juniors())
                .middles(devCnt.middles())
                .seniors(devCnt.seniors())
                .situationText(turn.getSituation())
                .missionId(game.getMission().getId())
                .superEmployees(cLevels)
                .officeName(office.getName())
                .endTime(game.getEndTime())
                .finalScore(finalScore)
                .build();
    }

    public RollResponse rollDice(Long gameId) {
        gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameId));

        int d1 = random.nextInt(6) + 1;
        int d2 = random.nextInt(6) + 1;
        int total = d1 + d2;

        String zone;
        if (total <= 4) zone = "critical_fail";
        else if (total <= 6) zone = "fail";
        else if (total <= 9) zone = "neutral";
        else if (total <= 11) zone = "success";
        else zone = "critical_success";

        return new RollResponse(total, d1, d2, zone);
    }

    private int clamp(int val) {
        return Math.min(100, Math.max(0, val));
    }

    private GameContext loadGameContext(Long gameId) {
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
            endGameWithTransaction(gameContext.getGame().getId());
        }
    }

    private RollWithMultiplier doRoll(Long gameId) {
        RollResponse roll = rollDice(gameId);
        int total = roll.getDiceTotal();

        double multiplier = switch (total) {
            case 1 -> 0.70;
            case 2, 3 -> 0.85;
            case 4, 5 -> 0.95;
            case 8, 9 -> 1.05;
            case 10, 11 -> 1.15;
            case 12 -> 1.30;
            default -> 1.00;
        };
        return new RollWithMultiplier(roll, multiplier);
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

    private void applyChanges(Resources resources, ResourceDelta resourcesDelta, double multiplier) {
        resources.setMotivation(clamp((int) Math.round((resources.getMotivation() + resourcesDelta.getMotivation()) * multiplier)));
        resources.setTechnicReadiness(clamp((int) Math.round((resources.getTechnicReadiness() + resourcesDelta.getTechnicalReadiness()) * multiplier)));
        resources.setProductReadiness(clamp((int) Math.round((resources.getProductReadiness() + resourcesDelta.getProductReadiness()) * multiplier)));
        resources.setMoney(Math.max(0L, Math.round((resources.getMoney() + resourcesDelta.getMoney()) * multiplier)));
        resourcesRepository.save(resources);
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

    private int getMonthsPassed(Turn turn) {
        int tn = turn.getTurnNumber();
        int s1 = Math.min(tn, 6);
        int s2 = Math.min(Math.max(tn - 6, 0), 6);
        int s3 = Math.min(Math.max(tn - 12, 0), 6);
        return s1 + s2 * 3 + s3 * 6;
    }

    private void endGameWithTransaction(Long gameId) {
        GameService self = (GameService) AopContext.currentProxy();
        self.finalizeGame(gameId);
    }
}
