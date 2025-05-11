# Конфигурация безопасности Spring Security

Конфигурация класса SecurityConfig реализует stateless аутентификацию на основе JWT и отключает формальную авторизацию и сессии:

````java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/refresh", "/api/auth/status").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
````
- Все защищённые эндпоинты требуют валидного access-токена

- Stateless-сессии: отключена серверная сессия и form-login

- CORS и CSRF отключены вручную

- JwtAuthenticationFilter подключается перед стандартным фильтром аутентификации UsernamePasswordAuthenticationFilter