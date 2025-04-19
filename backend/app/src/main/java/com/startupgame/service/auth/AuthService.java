package com.startupgame.service.auth;

import com.startupgame.dto.auth.AuthResponse;
import com.startupgame.dto.auth.LoginRequest;
import com.startupgame.dto.auth.RegisterRequest;
import com.startupgame.entity.auth.RefreshToken;
import com.startupgame.entity.user.User;
import com.startupgame.exception.UserAlreadyExistsException;
import com.startupgame.repository.auth.RefreshTokenRepository;
import com.startupgame.repository.user.UserRepository;
import com.startupgame.security.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;


    /**
     * Регистрирует нового пользователя на основе переданных данных.
     * <p>
     * Последовательность действий:
     * <ul>
     *     <li>Проверяет, существует ли уже пользователь с таким username или email</li>
     *     <li>Создаёт и сохраняет нового пользователя в базе данных</li>
     *     <li>Генерирует access и refresh токены (JWT)</li>
     *     <li>Сохраняет refresh токен в базе данных для последующего обновления</li>
     *     <li>Возвращает объект {@link AuthResponse} с access/refresh токенами и именем пользователя</li>
     * </ul>
     * </p>
     *
     * @param request объект {@link RegisterRequest}, содержащий данные для регистрации: username, email, пароль
     * @return {@link AuthResponse} с JWT access и refresh токенами
     * @throws UserAlreadyExistsException если пользователь с таким username или email уже существует
     */
    public AuthResponse registerNewUser(RegisterRequest request) {
        log.info("Registering new user: username={}, email={}",
                request.getUsername(), request.getEmail());
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration denied: username '{}' is already in use", request.getUsername());
            throw new UserAlreadyExistsException("Username is already in use");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration denied: email '{}' is already in use", request.getEmail());
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        log.info("User '{}' successfully registered", user.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        saveRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    /**
     * Обновляет access токен по действующему refresh токену.
     * <p>
     * Метод выполняет следующие шаги:
     * <ul>
     *     <li>Проверяет наличие refresh токена в базе данных</li>
     *     <li>Извлекает имя пользователя из переданного токена</li>
     *     <li>Загружает данные пользователя по имени</li>
     *     <li>Валидирует refresh токен: проверяет подпись и срок действия</li>
     *     <li>Если токен валиден — генерирует и возвращает новый access токен</li>
     *     <li>Если токен невалиден — удаляет его из базы и выбрасывает исключение</li>
     * </ul>
     * </p>
     *
     * @param refreshToken JWT refresh токен, полученный от клиента
     * @return новый access токен (JWT) для авторизации пользователя
     * @throws RuntimeException если токен не найден, невалиден или просрочен
     */
    public String refreshAccessToken(String refreshToken) {
        RefreshToken entity = refreshTokenRepository.findByTokenValue(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found in the database");
                    return new RuntimeException("Invalid refresh token");
                });

        String username = jwtUtil.extractUsername(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            refreshTokenRepository.delete(entity);
            throw new RuntimeException("Refresh token is invalid or expired");
        }

        return jwtUtil.generateAccessToken(userDetails);
    }


    /**
     * Аутентифицирует пользователя по предоставленным email и паролю.
     * <p>
     * Если аутентификация проходит успешно:
     * <ul>
     *     <li>Создаётся access токен (JWT) для авторизации</li>
     *     <li>Создаётся refresh токен (JWT), который сохраняется в базу данных</li>
     *     <li>Возвращается объект {@link AuthResponse} с access/refresh токенами и именем пользователя</li>
     * </ul>
     * </p>
     *
     * @param request объект {@link LoginRequest}, содержащий email и пароль пользователя
     * @return {@link AuthResponse} с access и refresh токенами
     * @throws RuntimeException если пользователь не найден в базе после успешной аутентификации
     */
    public AuthResponse authenticate(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        saveRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(User user, String token) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken entity = RefreshToken.builder()
                .user(user)
                .tokenValue(token)
                .build();
        try {
            refreshTokenRepository.save(entity);
            log.info("Refresh token saved successfully for user '{}'", user.getUsername());
        } catch (Exception e) { // TODO add custom exception
            log.error("Failed to save refresh token for user '{}': {}", user.getUsername(), e.getMessage(), e);
        }
        log.info("Access and refresh tokens generated and saved for user '{}'", user.getUsername());
    }
}
