package com.startupgame.controller;

import com.startupgame.dto.game.ThemeDTO;
import com.startupgame.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/getThemes")
    public ResponseEntity<List<ThemeDTO>> getThemes() {
        List<ThemeDTO> themes = gameService.getThemes();
        return ResponseEntity.ok(themes);
    }
}
