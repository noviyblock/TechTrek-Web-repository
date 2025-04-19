package com.startupgame.service.game;

import com.startupgame.dto.game.GameStateDTO;
import com.startupgame.dto.game.SphereDTO;
import com.startupgame.entity.game.*;
import com.startupgame.entity.user.User;
import com.startupgame.repository.game.*;
import com.startupgame.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public List<SphereDTO> getThemes() {
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
                    .build());

            Turn firstTurn = turnRepository.save(Turn.builder()
                    .game(game)
                    .turnNumber(0)
                    .stage(1)
                    .resources(resources)
                    .situation("Начало игры")
                    .build());

            return buildGameStateDTO(game, firstTurn, resources, team);
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
                .numberOfOffices(resources.getNumberOfOffices())
                .build();
    }
}
