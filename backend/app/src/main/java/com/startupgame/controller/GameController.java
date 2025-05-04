package com.startupgame.controller;

import com.startupgame.dto.game.*;
import com.startupgame.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/spheres")
    public ResponseEntity<List<SphereDTO>> getThemes() {
        List<SphereDTO> spheres = gameService.getSpheres();
        return ResponseEntity.ok(spheres);
    }

    @PostMapping("/start")
    public ResponseEntity<GameStateDTO> startGame(@RequestBody StartGameRequest request, Authentication authentication) {
        GameStateDTO dto = gameService.startGame(request.getMissionId(),
                request.getCompanyName(),
                authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{gameId}/state")
    public GameStateDTO getState(@PathVariable Long gameId) {
        return gameService.getCurrentState(gameId);
    }

    @GetMapping("/{gameId}/modifiers")
    public ResponseEntity<List<ModifierResponse>> getModifiers(@PathVariable Long gameId) {
        List<ModifierResponse> modifierResponses = gameService.getAllModifiers(gameId);
        return ResponseEntity.ok(modifierResponses);
    }

    @PostMapping("/{gameId}/modifiers")
    public ResponseEntity<PurchaseResponse> purchaseModifier(@PathVariable Long gameId, @RequestBody PurchaseRequest request) {
        PurchaseResponse resp = gameService.purchaseModifier(gameId, request.getModifierId());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{gameId}/evaluate-decision")
    public ResponseEntity<EvaluateDecisionResponse> evaluateDecision(@PathVariable Long gameId, @RequestBody DecisionRequest req) {
        EvaluateDecisionResponse resp = gameService.evaluateDecision(gameId, req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{gameId}/evaluate-presentation")
    public EvaluateDecisionResponse evaluatePresentation(@PathVariable Long gameId, @RequestBody DecisionRequest decisionRequest) {
        return gameService.evaluatePresentation(gameId, decisionRequest);
    }
}
