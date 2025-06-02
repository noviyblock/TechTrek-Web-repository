package com.startupgame.service;

import org.springframework.data.domain.PageImpl;

import com.startupgame.modules.game.entity.Sphere;
import com.startupgame.modules.game.repository.SphereRepository;
import com.startupgame.modules.game.service.SphereService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SphereServiceTest {

    @Mock
    private SphereRepository sphereRepository;

    @InjectMocks
    private SphereService sphereService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSpheres_shouldReturnPagedSpheres() {
        int page = 0;
        int size = 7;

        List<Sphere> spheres = List.of(new Sphere(), new Sphere(), new Sphere());
        Pageable pageable = PageRequest.of(page, size);

        when(sphereRepository.findAll(pageable)).thenReturn(new PageImpl<>(spheres)); // Используем PageImpl

        List<Sphere> result = sphereService.getSpheres(page, size);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(sphereRepository, times(1)).findAll(pageable);
    }

    @Test
    void updateSpheres_shouldReturnNextSetOfSpheres() {
        int offset = 7;
        int size = 7;

        List<Sphere> spheres = List.of(new Sphere(), new Sphere());
        Pageable pageable = PageRequest.of(offset / size, size);

        when(sphereRepository.findAll(pageable)).thenReturn(new PageImpl<>(spheres)); // Используем PageImpl

        List<Sphere> result = sphereService.updateSpheres(offset);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sphereRepository, times(1)).findAll(pageable);
    }

}
