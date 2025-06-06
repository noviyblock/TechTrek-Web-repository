package com.startupgame.modules.auth.service;

import com.startupgame.modules.auth.dto.response.AccessTokenResponse;
import com.startupgame.modules.auth.dto.response.AuthResponse;
import com.startupgame.modules.auth.dto.request.LoginRequest;
import com.startupgame.modules.auth.dto.request.RegisterRequest;
import com.startupgame.modules.auth.entity.RefreshToken;
import com.startupgame.modules.user.Role;
import com.startupgame.modules.user.User;
import com.startupgame.core.exception.InvalidTokenException;
import com.startupgame.core.exception.NotFoundExecption;
import com.startupgame.core.exception.UserAlreadyExistsException;
import com.startupgame.modules.auth.repository.RefreshTokenRepository;
import com.startupgame.modules.user.UserRepository;
import com.startupgame.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.startupgame.service.auth.EmailService;
import com.startupgame.service.auth.OtpService;

import java.util.UUID;


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
    private final EmailService emailService;
    private final OtpService otpService;

    public AuthResponse verifyOtpAndGenerateTokens(String email, String otp) {
        if (!otpService.validateOtp(email, otp)) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        saveRefreshToken(user, refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }


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

        // MDC.put("username", request.getUsername());
        // MDC.put("userId", String.valueOf(user.getId()));
        // log.info("User '{}' successfully registered", user.getUsername());

        // UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // String accessToken = jwtUtil.generateAccessToken(userDetails);
        // String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // saveRefreshToken(user, refreshToken);
        // MDC.clear();
        // return new AuthResponse(accessToken, refreshToken);

        // Генерация и отправка OTP
        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);
        log.info("OTP sent to email: {}", user.getEmail());

        return new AuthResponse(null, null); // Токены ещё не выдаём
    }

    public AuthResponse createGuestSession() {
        String guestId = "guest-" + UUID.randomUUID();
        log.info("Creating guest session: guestId={}", guestId);

        User guest = User.builder()
                .username(guestId)
                .email("")
                .password("")
                .role(Role.ROLE_GUEST)
                .build();

        userRepository.save(guest);

        UserDetails guestDetails = org.springframework.security.core.userdetails.User
                .withUsername(guest.getUsername())
                .password("")
                .authorities(guest.getRole().name())
                .build();

        String accessToken = jwtUtil.generateAccessToken(guestDetails);
        String refreshToken = jwtUtil.generateRefreshToken(guestDetails);

        return new AuthResponse(accessToken, refreshToken);
    }

    /**
     * Обновляет access токен по-действующему refresh токену.
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
     * @throws NotFoundExecption если токен не найден, невалиден или просрочен
     */
    public AccessTokenResponse refreshAccessToken(String refreshToken) {
        RefreshToken entity = refreshTokenRepository.findByTokenValue(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found in the database");
                    return new RuntimeException("Invalid refresh token");
                });

        String username = jwtUtil.extractUsername(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            refreshTokenRepository.delete(entity);
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        return new AccessTokenResponse(newAccessToken);
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
     * @throws NotFoundExecption если пользователь не найден в базе после успешной аутентификации
     */
    @Transactional
    public AuthResponse authenticate(LoginRequest request) {
        log.debug("Login requested: {}", request.getEmail());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundExecption("User not found"));

        saveRefreshToken(user, refreshToken);

        MDC.put("username", user.getUsername());
        MDC.put("userId", String.valueOf(user.getId()));
        log.info("User {} authenticated successfully", user.getId());
        MDC.clear();
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

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByTokenValue(refreshToken);
    }
}
