package com.startupgame.modules.game.controller;

import com.startupgame.modules.game.dto.game.request.DecisionRequest;
import com.startupgame.modules.game.dto.game.request.PurchaseRequest;
import com.startupgame.modules.game.dto.game.request.StartGameRequest;
import com.startupgame.modules.game.dto.game.response.*;
import com.startupgame.modules.game.service.*;
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
    private final GameLifecycleService gameLifecycleService;
    private final ModifierService modifierService;
    private final DecisionService decisionService;
    private final CrisisService crisisService;

    @GetMapping("/spheres")
    public ResponseEntity<List<SphereDTO>> getThemes() {
        List<SphereDTO> spheres = gameService.getSpheres();
        return ResponseEntity.ok(spheres);
    }

    @PostMapping("/start")
    public ResponseEntity<GameStateDTO> startGame(@RequestBody StartGameRequest request, Authentication authentication) {
        GameStateDTO dto = gameLifecycleService.startGame(request.getMissionId(),
                request.getCompanyName(),
                authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{gameId}/state")
    public GameStateDTO getState(@PathVariable Long gameId) {
        return gameLifecycleService.getCurrentState(gameId);
    }

    @GetMapping("/{gameId}/modifiers")
    public ResponseEntity<List<ModifierResponse>> getModifiers(@PathVariable Long gameId) {
        List<ModifierResponse> modifierResponses = modifierService.getAllModifiers(gameId);
        return ResponseEntity.ok(modifierResponses);
    }

    @PostMapping("/{gameId}/modifiers")
    public ResponseEntity<PurchaseResponse> purchaseModifier(@PathVariable Long gameId, @RequestBody PurchaseRequest request) {
        PurchaseResponse resp = modifierService.purchaseModifier(gameId, request.getModifierId());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{gameId}/evaluate-decision")
    public ResponseEntity<EvaluateDecisionResponse> evaluateDecision(@PathVariable Long gameId, @RequestBody DecisionRequest req) {
        EvaluateDecisionResponse resp = decisionService.evaluateDecision(gameId, req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{gameId}/evaluate-presentation")
    public EvaluateDecisionResponse evaluatePresentation(@PathVariable Long gameId, @RequestBody DecisionRequest decisionRequest) {
        return decisionService.evaluatePresentation(gameId, decisionRequest);
    }

    @PostMapping("/{gameId}/generate-crisis")
    public CrisisResponse generateCrisis(@PathVariable Long gameId) {
        return crisisService.generateCrisis(gameId);
    }
}
