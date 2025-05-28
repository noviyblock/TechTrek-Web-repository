package com.startupgame.modules.game.service;

import com.startupgame.modules.game.dto.game.response.*;
import com.startupgame.modules.game.entity.*;
import com.startupgame.modules.game.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.startupgame.core.util.GameUtil.getMonthsPassed;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final SphereRepository themeRepository;
    private final GameRepository gameRepository;
    private final ResourcesRepository resourcesRepository;
    private final TurnRepository turnRepository;
    private final GameFinalScoreRepository gameFinalScoreRepository;
    private final GameContextLoader gameContextLoader;

    public List<SphereDTO> getSpheres() {
        List<Sphere> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> new SphereDTO(theme.getId(), theme.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void finalizeGame(Long gameId) {
        log.info("Finalizing game {}", gameId);

        GameContext ctx = gameContextLoader.load(gameId);
        Game game = ctx.getGame();

        if (game.getEndTime() == null) {
            game.setEndTime(LocalDateTime.now());
            gameRepository.save(game);
        }

        int minMotivation = turnRepository.findAllByGameId(gameId).stream()
                .mapToInt(t -> resourcesRepository.findById(t.getResources().getId())
                        .orElseThrow()
                        .getMotivation())
                .min()
                .orElse(ctx.getResources().getMotivation());

        boolean motivationNeverBelow50 = minMotivation >= 50;

        int monthsPassed = getMonthsPassed(ctx.getTurn());

        FinalScoreDTO score = FinalScoreCalculator.calculate(
                ctx.getResources().getMoney(),
                ctx.getResources().getTechnicReadiness(),
                ctx.getResources().getProductReadiness(),
                ctx.getResources().getMotivation(),
                monthsPassed,
                false, //TODO
                motivationNeverBelow50
        );

        GameFinalScore gameFinalScore = getGameFinalScore(game, score);
        gameFinalScoreRepository.save(gameFinalScore);

        game.setScore(score.totalScore());
        gameRepository.save(game);

    }

    private static GameFinalScore getGameFinalScore(Game game, FinalScoreDTO score) {
        GameFinalScore gameFinalScore = new GameFinalScore();
        gameFinalScore.setGame(game);
        gameFinalScore.setMoneyScore(score.moneyScore());
        gameFinalScore.setTechScore(score.techScore());
        gameFinalScore.setProductScore(score.productScore());
        gameFinalScore.setMotivationScore(score.motivationScore());
        gameFinalScore.setTimeScore(score.timeScore());
        gameFinalScore.setBonusScore(score.bonusScore());
        gameFinalScore.setTotalScore(score.totalScore());
        return gameFinalScore;
    }
}
