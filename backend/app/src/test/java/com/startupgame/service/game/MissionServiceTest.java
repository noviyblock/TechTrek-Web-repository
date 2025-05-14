package com.startupgame.service.game;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.startupgame.entity.game.Mission;
import com.startupgame.repository.game.MissionRepository;

@ExtendWith(SpringExtension.class)
class MissionServiceTest {

    @InjectMocks
    private MissionService missionService;

    @Mock
    private MissionRepository missionRepository;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 4);
    }

    @Test
    void getMissionsBySphere_returnsPageOfMissions() {
        Mission mission = new Mission();
        Page<Mission> missionPage = new PageImpl<>(Arrays.asList(mission));

        when(missionRepository.findBySphereId(1L, pageable)).thenReturn(missionPage);

        Page<Mission> result = missionService.getMissionsBySphere(1L, 0, 4);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(missionRepository, times(1)).findBySphereId(1L, pageable);
    }

    @Test
    void updateMissionsBySphere_returnsListOfMissions() {
        Mission mission = new Mission();
        Page<Mission> missionPage = new PageImpl<>(Arrays.asList(mission));

        when(missionRepository.findBySphereId(1L, pageable)).thenReturn(missionPage);

        List<Mission> result = missionService.updateMissionsBySphere(1L, 0, 4);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(missionRepository, times(1)).findBySphereId(1L, pageable);
    }
}