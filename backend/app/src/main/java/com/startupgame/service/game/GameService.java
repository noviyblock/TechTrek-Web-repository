package com.startupgame.service.game;

import com.startupgame.dto.game.SphereDTO;
import com.startupgame.entity.Sphere;
import com.startupgame.repository.SphereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.startupgame.entity.Game;
import com.startupgame.entity.Mission;
import com.startupgame.repository.GameRepository;
import com.startupgame.repository.MissionRepository;
@Service
public class GameService {
    @Autowired
    private SphereRepository themeRepository;
    
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MissionRepository missionRepository;

    public List<SphereDTO> getThemes() {
        List<Sphere> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> new SphereDTO(theme.getId(), theme.getName()))
                .collect(Collectors.toList());
    }

    // Новый метод startGame
    public Game startGame(Long missionId, String companyName, Long userId) {
        // Получаем миссию по ID
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));

        // Создаем новый объект Game
        Game newGame = new Game();
        newGame.setTeamName(companyName);  // устанавливаем название команды (companyName)
        newGame.setDifficulty(1.0);  //по умолчанию 1.0
        newGame.setMission(mission);  // связываем игру с миссией

        // сохраняем  в бд
        return gameRepository.save(newGame);
    }
}
