package com.startupgame.modules.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.startupgame.modules.game.service.SphereService;
import com.startupgame.modules.game.entity.Sphere;

@RestController
@RequiredArgsConstructor
public class SphereController {

    private final SphereService sphereService;

    @GetMapping("/spheres")
    public List<Sphere> getSpheres(@RequestParam int page, @RequestParam int size) {
        return sphereService.getSpheres(page, size);
    }
}
