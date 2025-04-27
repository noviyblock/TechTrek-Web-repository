package com.startupgame.client;

import com.startupgame.dto.ml.EvaluateDecisionRequest;
import com.startupgame.dto.ml.EvaluateDecisionResult;
import com.startupgame.dto.ml.NewGameRequest;
import com.startupgame.dto.ml.NewGameResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

public interface MLApiClient {
    @HttpExchange(method = "POST", url = "/game/new")
    NewGameResponse createGame(@RequestBody NewGameRequest req);

    @HttpExchange(method = "POST", url = "/game/evaluate_decision")
    EvaluateDecisionResult evaluateDecision(@RequestBody EvaluateDecisionRequest req);
}
