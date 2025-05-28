package com.startupgame.client;

import com.startupgame.modules.game.dto.game.response.CrisisResponse;
import com.startupgame.modules.game.dto.ml.request.*;
import com.startupgame.modules.game.dto.ml.response.EvaluateDecisionResult;
import com.startupgame.modules.game.dto.ml.response.GeneratedMissionResponse;
import com.startupgame.modules.game.dto.ml.response.NewGameResponse;
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

    @HttpExchange(method = "POST", url = "/game/generate_missions")
    GeneratedMissionResponse generateMission(@RequestBody GeneratedMissionRequest req);
}
