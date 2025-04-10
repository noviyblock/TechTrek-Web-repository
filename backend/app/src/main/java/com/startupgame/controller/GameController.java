package com.startupgame.controller;

import com.startupgame.dto.game.SphereDTO;
import com.startupgame.entity.Game;
import com.startupgame.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.startupgame.dto.StartGameRequest;
import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {


    private final GameService gameService;

    @GetMapping("/getThemes")
    public ResponseEntity<List<SphereDTO>> getThemes() {
        List<SphereDTO> themes = gameService.getThemes();
        return ResponseEntity.ok(themes);
    }

    @PostMapping("/game/start")
    public Game startGame(@RequestBody StartGameRequest request) {
        return gameService.startGame(request.getMissionId(), request.getCompanyName(), request.getUserId());
    }
}
