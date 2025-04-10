package com.startupgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.startupgame.service.game.SphereService;
import com.startupgame.entity.Sphere;

@RestController
public class SphereController {

    @Autowired
    private SphereService sphereService;

    @GetMapping("/spheres")
    public List<Sphere> getSpheres(@RequestParam int page, @RequestParam int size) {
        return sphereService.getSpheres(page, size);
    }
}
