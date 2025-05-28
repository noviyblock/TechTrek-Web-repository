package com.startupgame.modules.game.controller;


import com.startupgame.modules.game.dto.game.request.CreateMissionRequest;
import com.startupgame.modules.game.dto.game.response.CreateMissionResponse;
import com.startupgame.modules.game.dto.ml.response.GeneratedMissionResponse;
import com.startupgame.modules.game.entity.Mission;
import com.startupgame.modules.game.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/missions")
    public List<Mission> getMissionsBySphere(@RequestParam Long sphereId, @RequestParam int page, @RequestParam int size) {
        return missionService.getMissionsBySphere(sphereId, page, size).getContent();
    }

    @PostMapping("/generate-mission")
    public ResponseEntity<GeneratedMissionResponse> generateMission(@RequestParam Long sphereId) {
        return ResponseEntity.ok(missionService.generateMission(sphereId));
    }

    @PostMapping("/pick-mission")
    public ResponseEntity<CreateMissionResponse> createMission(@RequestBody CreateMissionRequest request) {
        CreateMissionResponse createMissionResponse = missionService.createMission(request);
        return ResponseEntity.ok(createMissionResponse);
    }

}

