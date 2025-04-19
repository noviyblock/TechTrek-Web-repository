package com.startupgame.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import com.startupgame.repository.game.SphereRepository;
import com.startupgame.entity.game.Sphere;

@Service
public class SphereService {

    @Autowired
    private SphereRepository sphereRepository;

    /**
     * Получает сферы с пагинацией.
     *
     * @param page Страница пагинации.
     * @param size Количество элементов на странице.
     * @return Список сфер.
     */
    public List<Sphere> getSpheres(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Создаем объект Pageable для пагинации
        return sphereRepository.findAll(pageable).getContent();  // Получаем контент из страницы
    }

    /**
     * Обновляет список сфер, возвращая следующие 7 записей.
     *
     * @return Список сфер.
     */
    public List<Sphere> updateSpheres(int offset) {
        Pageable pageable = PageRequest.of(offset / 7, 7);  // Пагинация с учетом сдвига
        return sphereRepository.findAll(pageable).getContent();  // Получаем контент из страницы
    }
}
