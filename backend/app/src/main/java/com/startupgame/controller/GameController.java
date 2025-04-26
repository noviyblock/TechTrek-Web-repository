package com.startupgame.controller;

import com.startupgame.dto.game.GameStateDTO;
import com.startupgame.dto.game.SphereDTO;
import com.startupgame.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.startupgame.dto.game.StartGameRequest;

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

    @PostMapping("/start")
    public ResponseEntity<GameStateDTO> startGame(@RequestBody StartGameRequest request, Authentication authentication) {
        String username = authentication.getName();
        GameStateDTO dto = gameService.startGame(request.getMissionId(),
                request.getCompanyName(),
                authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
