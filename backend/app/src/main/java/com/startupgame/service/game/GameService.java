package com.startupgame.service.game;

import com.startupgame.client.MLApiClient;
import com.startupgame.dto.game.*;
import com.startupgame.dto.ml.EvaluateDecisionRequest;
import com.startupgame.dto.ml.EvaluateDecisionResult;
import com.startupgame.dto.ml.NewGameRequest;
import com.startupgame.dto.ml.NewGameResponse;
import com.startupgame.entity.game.*;
import com.startupgame.entity.user.User;
import com.startupgame.exception.InsufficientFundsException;
import com.startupgame.repository.game.*;
import com.startupgame.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
        NewGameRequest ngReq = new NewGameRequest();
        ngReq.setMoney(100000L);
        ngReq.setTechnicReadiness(0);
        ngReq.setProductReadiness(0);
        ngReq.setMotivation(50);
        NewGameResponse ngResp = mlApiClient.createGame(ngReq);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));

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
                .mlGameId(ngResp.getGame_id())
                .build());

        gameModifierService.initializeDefaultModifiersForGame(game);

        Turn firstTurn = turnRepository.save(Turn.builder()
                .game(game)
                .turnNumber(0)
                .stage(1)
                .resources(resources)
                .situation("Начало игры")
                .build());

        return buildGameStateDTO(game, firstTurn, resources, team);
    }

    @Transactional
    public GameStateDTO getCurrentState(Long gameId) {
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

        Team team = teamRepository.findById(game.getTeam().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Team for game id=%d not found", gameId)));

        return buildGameStateDTO(game, currentTurn, currentResources, team);
    }

    public List<ModifierResponse> getAllModifiers(Long gameId) {
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
                        .type(mod.getType())
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

        Resources resources = currentTurn.getResources();

        Long cost = modifier.getPurchaseCost();
        if (resources.getMoney() < cost) {
            throw new InsufficientFundsException(
                    String.format("Not enough funds: have %d but need %d",
                            resources.getMoney(), cost));
        }

        resources.setMoney(resources.getMoney() - cost);
        resourcesRepository.save(resources);
        //TODO we can buy many times only developers
        GameModifier item = new GameModifier(game, modifier);
        gameModifierRepository.save(item);

        List<String> owned = gameModifierRepository.findByGameId(gameId)
                .stream()
                .map(gm -> gm.getModifier().getName())
                .collect(Collectors.toList());

        return new PurchaseResponse(
                gameId,
                modifierId,
                resources.getMoney(),
                owned
        );
    }

    public EvaluateDecisionResponse evaluateDecision(Long gameId, DecisionRequest req) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameId));
        UUID mlUuid = game.getMlGameId();
        Turn currentTurn = turnRepository.findTopByGameIdOrderByTurnNumberDesc(gameId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No turns found for game id=" + gameId));
        Resources resources = resourcesRepository.findById(currentTurn.getResources().getId())
                .orElseThrow(() -> new EntityNotFoundException("Resources not found"));
        Team team = teamRepository.findById(game.getTeam().getId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        List<SuperEmployee> cLevels = superEmployeeRepository.findByTeamId(team.getId());

        EvaluateDecisionRequest mlRequest = new EvaluateDecisionRequest(
                mlUuid.toString(),
                resources.getMoney(),
                resources.getTechnicReadiness(),
                resources.getProductReadiness(),
                resources.getMotivation(),
                team.getJuniorAmount(),
                team.getMiddleAmount(),
                team.getSeniorAmount(),
                cLevels.stream().map(SuperEmployee::getName).collect(Collectors.toList()),
                req.getDecision()
        );
        EvaluateDecisionResult mlResp = mlApiClient.evaluateDecision(mlRequest);

        double score = mlResp.getQuality_score();
        int motivationDelta, techReadinessDelta, productReadinessDelta;
        double moneyDelta;
        if (score >= 0.8) {
            motivationDelta = 15;
            techReadinessDelta = 10;
            productReadinessDelta = 8;
            moneyDelta = 20000;
        } else if (score >= 0.6) {
            motivationDelta = 10;
            techReadinessDelta = 7;
            productReadinessDelta = 5;
            moneyDelta = 10000;
        } else if (score >= 0.4) {
            motivationDelta = 0;
            techReadinessDelta = 0;
            productReadinessDelta = 0;
            moneyDelta = 0;
        } else if (score >= 0.2) {
            motivationDelta = -5;
            techReadinessDelta = -3;
            productReadinessDelta = -2;
            moneyDelta = -10000;
        } else {
            motivationDelta = -10;
            techReadinessDelta = -5;
            productReadinessDelta = -4;
            moneyDelta = -20000;
        }

        resources.setMotivation(resources.getMotivation() + motivationDelta);
        resources.setTechnicReadiness(resources.getTechnicReadiness() + techReadinessDelta);
        resources.setProductReadiness(resources.getProductReadiness() + productReadinessDelta);
        resources.setMoney((long) (resources.getMoney() + moneyDelta));

        resourcesRepository.save(resources);

        return new EvaluateDecisionResponse(
                motivationDelta, resources.getMotivation(),
                techReadinessDelta, resources.getTechnicReadiness(),
                productReadinessDelta, resources.getProductReadiness(),
                moneyDelta, resources.getMoney(),
                mlResp.getText_to_player(),
                score
        );
    }


    private GameStateDTO buildGameStateDTO(Game game, Turn turn, Resources resources, Team team) {
        List<SuperEmployee> superEmployees = superEmployeeRepository.findByTeamId(team.getId());

        List<String> superEmployeeNames = superEmployees.stream()
                .map(SuperEmployee::getName)
                .toList();

        return GameStateDTO.builder()
                .gameId(game.getId())
                .companyName(game.getCompanyName())
                .stage(turn.getStage())
                .turnNumber(turn.getTurnNumber())
                .monthsPassed(turn.getTurnNumber() * 6)
                .money(resources.getMoney())
                .technicReadiness(resources.getTechnicReadiness())
                .productReadiness(resources.getProductReadiness())
                .motivation(resources.getMotivation())
                .juniors(team.getJuniorAmount())
                .middles(team.getMiddleAmount())
                .seniors(team.getSeniorAmount())
                .situationText(turn.getSituation())
                .missionId(game.getMission().getId())
                .superEmployees(superEmployeeNames)
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

        return new RollResponse(total, zone);
    }
}
