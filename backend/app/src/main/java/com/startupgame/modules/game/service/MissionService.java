package com.startupgame.modules.game.service;

import com.startupgame.client.MLApiClient;
import com.startupgame.modules.game.dto.game.request.CreateMissionRequest;
import com.startupgame.modules.game.dto.game.response.CreateMissionResponse;
import com.startupgame.modules.game.dto.ml.request.GeneratedMissionRequest;
import com.startupgame.modules.game.dto.ml.response.GeneratedMissionResponse;
import com.startupgame.modules.game.entity.Sphere;
import com.startupgame.core.exception.NotFoundExecption;
import com.startupgame.modules.game.repository.SphereRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.startupgame.modules.game.repository.MissionRepository;
import com.startupgame.modules.game.entity.Mission;
import org.springframework.data.domain.Page;

@Service
@Slf4j
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final SphereRepository sphereRepository;
    private final MLApiClient mlApiClient;

    /**
     * Получает миссии для определенной сферы с пагинацией.
     *
     * @param sphereId Идентификатор сферы.
     * @param page     Страница пагинации.
     * @param size     Количество миссий на странице.
     * @return Список миссий.
     */
    public Page<Mission> getMissionsBySphere(Long sphereId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // создаем объект Pageable
        return missionRepository.findBySphereId(sphereId, pageable);  // передаем Pageable
    }

    /**
     * Обновляет список миссий для сферы, возвращая следующие 4 миссии.
     *
     * @param sphereId Идентификатор сферы.
     * @param page     Страница пагинации.
     * @param size     Количество миссий на странице.
     * @return Список миссий.
     */
    public List<Mission> updateMissionsBySphere(Long sphereId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // создаем объект Pageable
        return missionRepository.findBySphereId(sphereId, pageable).getContent();  // извлекаем содержимое из Page
    }

    public GeneratedMissionResponse generateMission(Long sphereId) {
        log.info("Generate mission for sphere {}", sphereId);
        Sphere sphere = sphereRepository.findById(sphereId).orElseThrow(() -> new NotFoundExecption("Sphere not found"));
        GeneratedMissionRequest generatedMissionRequest = new GeneratedMissionRequest(
                sphere.getName()
        );
        return mlApiClient.generateMission(generatedMissionRequest);
    }


    @Transactional
    public CreateMissionResponse createMission(CreateMissionRequest request) {
        Sphere sphere = sphereRepository.findById(request.getSphereId())
                .orElseThrow(() -> new NotFoundExecption("Сфера не найдена"));

        Mission mission = new Mission();
        mission.setName(request.getTitle());
        mission.setSphere(sphere);
        missionRepository.save(mission);

        return new CreateMissionResponse(
                mission.getId(),
                mission.getName()
        );
    }
}
