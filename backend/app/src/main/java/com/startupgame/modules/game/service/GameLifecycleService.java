package com.startupgame.modules.game.service;

import com.startupgame.client.MLApiClient;
import com.startupgame.core.util.GameUtil;
import com.startupgame.modules.game.dto.game.response.DeveloperCounts;
import com.startupgame.modules.game.dto.game.response.FinalScoreDTO;
import com.startupgame.modules.game.dto.game.response.GameContext;
import com.startupgame.modules.game.dto.game.response.GameStateDTO;
import com.startupgame.modules.game.dto.ml.request.NewGameRequest;
import com.startupgame.modules.game.dto.ml.response.NewGameResponse;
import com.startupgame.modules.user.User;
import com.startupgame.core.exception.MlServiceUnavailableException;
import com.startupgame.modules.game.entity.*;
import com.startupgame.modules.game.repository.*;
import com.startupgame.modules.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameLifecycleService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final MLApiClient mlApiClient;
    private final ResourcesRepository resourcesRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final GameModifierService gameModifierService;
    private final TurnRepository turnRepository;
    private final GameModifierRepository gameModifierRepository;
    private final GameFinalScoreRepository gameFinalScoreRepository;
    private final GameUtil gameUtil;


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
                .monthsPassed(GameUtil.getMonthsPassed(turn))
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

    public boolean tryFinishEarly(GameContext ctx) {
        Resources resources = ctx.getResources();
        if (resources.getTechnicReadiness() >= 100 && resources.getProductReadiness() >= 100) {
            if (ctx.getGame().getEndTime() == null) {
                ctx.getGame().setEndTime(LocalDateTime.now());
                gameRepository.save(ctx.getGame());
            }
            gameUtil.endGameWithTransaction(ctx.getGame().getId());
            return true;
        }
        return false;
    }
}
