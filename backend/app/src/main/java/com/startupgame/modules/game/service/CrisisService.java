package com.startupgame.modules.game.service;

import com.startupgame.client.MLApiClient;
import com.startupgame.modules.game.dto.game.response.*;
import com.startupgame.modules.game.dto.ml.request.GenerateCrisisRequest;
import com.startupgame.modules.game.entity.Game;
import com.startupgame.modules.game.repository.GameModifierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.startupgame.core.util.GameUtil.getMonthsPassed;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrisisService {
    private final GameContextLoader gameContextLoader;
    private final GameModifierRepository gameModifierRepository;
    private final MLApiClient mlApiClient;
    private final YandexTranslateService yandexTranslateService;

    public CrisisResponse generateCrisis(Long gameId) {
        log.info("Generate crisis for game id: {}", gameId);

        GameContext gameContext = gameContextLoader.load(gameId);
        Game game = gameContext.getGame();

        DeveloperCounts devCnt = gameModifierRepository.findDeveloperCounts(game.getId());
        List<String> cLevels = gameModifierRepository.findCLevelNames(game.getId());

        ResourcesDTO res = new ResourcesDTO(
                gameContext.getResources().getMoney(),
                gameContext.getResources().getTechnicReadiness(),
                gameContext.getResources().getProductReadiness(),
                gameContext.getResources().getMotivation(),
                getMonthsPassed(gameContext.getTurn())
        );

        StaffsDTO staffs = new StaffsDTO(
                devCnt.juniors(),
                devCnt.middles(),
                devCnt.seniors(),
                cLevels
        );

        GenerateCrisisRequest generateCrisisRequest = new GenerateCrisisRequest(
                res,
                staffs,
                game.getMlGameId()
        );

        log.debug("Generate crisis request: {}", generateCrisisRequest);

        CrisisResponse response = mlApiClient.generateCrisis(generateCrisisRequest);

        if (response.getDescription() != null && !response.getDescription().isBlank()) {
            String translated = yandexTranslateService.translateText(response.getDescription(), "ru");
            response.setDescription(translated);
        }

        return response;
    }
}
