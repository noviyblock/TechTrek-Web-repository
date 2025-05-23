package com.startupgame.client;

import com.startupgame.dto.game.CrisisResponse;
import com.startupgame.dto.ml.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

public interface MLApiClient {
    @HttpExchange(method = "POST", url = "/game/new")
    NewGameResponse createGame(@RequestBody NewGameRequest req);

    @HttpExchange(method = "POST", url = "/game/evaluate_decision")
    EvaluateDecisionResult evaluateDecision(@RequestBody EvaluateDecisionRequest req);

    @HttpExchange(method = "POST", url = "/game/resolve")
    ResolveRequest resolveRequest(@RequestBody ResolveRequest req);

    @HttpExchange(method = "POST", url = "/game/generate_crisis")
    CrisisResponse generateCrisis(@RequestBody GenerateCrisisRequest req);
}
