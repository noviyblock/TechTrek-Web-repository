package com.startupgame.controller;


import com.startupgame.entity.Mission;
import com.startupgame.service.game.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping("/missions")
    public List<Mission> getMissionsBySphere(@RequestParam Long sphereId, @RequestParam int page, @RequestParam int size) {
        return missionService.getMissionsBySphere(sphereId, page, size).getContent();
    }
}

