package com.startupgame.service.game;

import com.startupgame.dto.game.ThemeDTO;
import com.startupgame.entity.Theme;
import com.startupgame.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    private ThemeRepository themeRepository;

    public List<ThemeDTO> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> new ThemeDTO(theme.getId(), theme.getName()))
                .collect(Collectors.toList());
    }
}
