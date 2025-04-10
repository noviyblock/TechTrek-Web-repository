package com.startupgame.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import com.startupgame.repository.MissionRepository;
import com.startupgame.entity.Mission;
import org.springframework.data.domain.Page;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

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
}
