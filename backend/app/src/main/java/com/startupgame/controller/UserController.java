package com.startupgame.controller;

import com.startupgame.dto.game.GameShortInfoDTO;
import com.startupgame.dto.user.UserMeResponse;
import com.startupgame.entity.user.User;
import com.startupgame.repository.game.GameRepository;
import com.startupgame.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(("/api/user"))
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUser(@AuthenticationPrincipal UserDetails currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<GameShortInfoDTO> games = gameRepository.findByUserId(user.getId()).stream()
                .map(game -> new GameShortInfoDTO(
                        game.getMission().getSphere().getName(),
                        83
                ))
                .collect(Collectors.toList());

        UserMeResponse response = new UserMeResponse(user.getUsername(), user.getEmail(), games);
        return ResponseEntity.ok(response);
    }
}
