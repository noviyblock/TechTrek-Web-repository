package com.startupgame.service.game;

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
     * @param missionId    идентификатор миссии
     * @param companyName  название компании
     * @param username     имя пользователя
     * @return объект {@link Game}, созданная игра
     * @throws EntityNotFoundException    если пользователь с указанным именем не найден
     * @throws IllegalArgumentException   если миссия с таким id отсутствует
     */
    @Transactional
    public Game startGame(Long missionId, String companyName, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));

        Game newGame = new Game();

        Resources resources = Resources.builder()
                .money(10000L)
                .motivation(50)
                .reputation(50)
                .productReadiness(0)
                .technicReadiness(0)
                .build();
        resourcesRepository.save(resources);
        Team team = Team.builder()
                .juniorAmount(0)
                .middleAmount(0)
                .seniorAmount(0)
                .build();
        teamRepository.save(team);

        Game game = Game.builder()
                .companyName(companyName)
                .difficulty(1.0)
                .mission(mission)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(null)
                .build();
        gameRepository.save(game);
        Turn firstTurn = Turn.builder()
                .game(game)
                .turnNumber(1)
                .stage(1)
                .resources(resources)
                .situation("Начало игры")
                .build();
        turnRepository.save(firstTurn);
        return game;
    }
}
