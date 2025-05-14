package com.startupgame.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.startupgame.dto.game.DecisionRequest;
import com.startupgame.dto.game.DeveloperCounts;
import com.startupgame.dto.game.EvaluateDecisionResponse;
import com.startupgame.dto.game.GameStateDTO;
import com.startupgame.dto.game.PurchaseResponse;
import com.startupgame.dto.game.ResourceDelta;
import com.startupgame.dto.game.SphereDTO;
import com.startupgame.dto.game.StartGameRequest;
import com.startupgame.dto.ml.EvaluateDecisionRequest;
import com.startupgame.service.game.GameService;

@SpringBootTest
@AutoConfigureMockMvc

class GameControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private StartGameRequest startGameRequest;

    @BeforeEach
    void setUp() {
        startGameRequest = new StartGameRequest();
        startGameRequest.setMissionId(1L);
        startGameRequest.setCompanyName("MyStartup");
    }

    @Test
    @WithMockUser(username = "tester")
    void testStartGame_returns201() throws Exception {
        GameStateDTO mockDto = GameStateDTO.builder()
                .gameId(1L)
                .companyName("MyStartup")
                .money(100000L)
                .stage(1)
                .turnNumber(1)
                .monthsPassed(0)
                .missionId(1L)
                .technicReadiness(50)
                .productReadiness(40)
                .motivation(70)
                .superEmployees(Collections.emptyList())
                .officeName(null)
                .endTime(null)
                .situationText("")
                .build();

        when(gameService.startGame(anyLong(), anyString(), anyString()))
                .thenReturn(mockDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"missionId":1,"companyName":"MyStartup"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId").value(1))
                .andExpect(jsonPath("$.money").value(100000));
        }

    @Test
    void testGetState_returnsGameStateDTO() throws Exception {
        GameStateDTO mockDto = GameStateDTO.builder()
            .gameId(1L)
            .companyName("MyStartup")
            .money(100000L)
            .stage(1)
            .turnNumber(1)
            .build();

        when(gameService.getCurrentState(1L)).thenReturn(mockDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/1/state"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId").value(1))
            .andExpect(jsonPath("$.companyName").value("MyStartup"));
    }

    @Test
    void testGetSpheres_returnsListOfSphereDTOs() throws Exception {
        SphereDTO sphere = new SphereDTO();
        sphere.setId(1L);
        sphere.setName("Tech");

        when(gameService.getSpheres()).thenReturn(Collections.singletonList(sphere));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/spheres"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Tech"));
    }

    @Test
    void testPurchaseModifier_returnsPurchaseResponse() throws Exception {
        PurchaseResponse response = new PurchaseResponse();
        response.setSuccess(true);
        response.setResourceDelta(new ResourceDelta(10000, 5, 5, 5));

        when(gameService.purchaseModifier(1L, 42L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/1/modifiers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modifierId\":42}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.resourceDelta.money").value(10000));
    }

    @Test
    void testEvaluateDecision_returnsEvaluationResponse() throws Exception {
        EvaluateDecisionRequest request = new EvaluateDecisionRequest();
        request.setActionType("HIRE_DEVELOPER");
        request.setDeveloperCounts(new DeveloperCounts(1, 0, 0));

        EvaluateDecisionResponse response = new EvaluateDecisionResponse();
        response.setResourceDelta(new ResourceDelta(-20000, 10, 0, 0));
        response.setSituation("You hired a junior developer.");
        response.setAnswer("Good choice!");
        response.setDiceNumber(7);

        when(gameService.evaluateDecision(anyLong(), any(DecisionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/1/evaluate-decision")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"actionType":"HIRE_DEVELOPER","developerCounts":{"junior":1}}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.situation").value("You hired a junior developer."))
            .andExpect(jsonPath("$.resourceDelta.money").value(-20000));
    }

    @Test
    void  testEvaluatePresentation_returnsEvaluationResponse() throws Exception {
        EvaluateDecisionRequest request = new EvaluateDecisionRequest();
        request.setActionType("PITCH_TO_INVESTORS");

        EvaluateDecisionResponse response = new EvaluateDecisionResponse();
        response.setResourceDelta(new ResourceDelta(50000, 0, 0, 0));
        response.setSituation("Investors loved your pitch.");
        response.setAnswer("Funding received.");
        response.setDiceNumber(11);

        when(gameService.evaluatePresentation(anyLong(), any(DecisionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/1/evaluate-presentation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"actionType":"PITCH_TO_INVESTORS"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.situation").value("Investors loved your pitch."))
            .andExpect(jsonPath("$.resourceDelta.money").value(50000));
    }
}