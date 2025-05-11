# Логика работы аутентификации

### JWT: Внутренняя реализация

#### Алгоритм

Используется HS256 (симметричный алгоритм HMAC с SHA-256).

Секретный ключ задается в application.yaml:

```yaml
jwt:
  secret: your-256-bit-secret
```

Ключ должен быть не короче 256 бит (32 символа в UTF-8), иначе библиотека jjwt выбросит исключение.

#### Генерация токенов

Метод `generateToken(UserDetails userDetails, long expiration)` создаёт JWT токен со следующими параметрами:
````java
Jwts.builder()
.claims(claims) // по умолчанию пустой Map
.subject(userDetails.getUsername()) // sub: "имя пользователя"
.issuedAt(new Date(now)) // iat
.expiration(new Date(now + expiration)) // exp
.signWith(getSecretKey()) // HMAC SHA256
.compact();
Сроки действия
- Access - 15 минут
- Refresh - 7 дней
````

#### Семантика содержимого токена
````json
{
  "sub": "john_doe",
  "iat": 1111111111,
  "exp": 1111111111
}
````
В текущей реализации в качестве идентификатора используется username, прочие атрибуты (например roles и userId) не включаются.

#### Алгоритм валидации токена

Функция `validateToken(String token, UserDetails userDetails)` выполняет верификацию следующим образом:

- Сопоставление поля sub (username) и имени пользователя UserDetails

- Актуальность токена: текущая дата должна быть меньше exp

#### Извлечение claim-значений

Для выборочного доступа к значениям в теле токена используется `ClaimsResolver`:

````java
String username = extractClaim(token, Claims::getSubject);
Date expiration = extractClaim(token, Claims::getExpiration);
````

#### Стратегия хранения и аннулирования Refresh-токенов

- Refresh токен сохраняется в базе данных в таблице `refresh_token` и ассоциируется с пользователем с помощью `user_id`

- При выходе (logout) или обновлении (refresh) он удаляется из базы

- Повторное использование аннулированного токена приводит к исключению (401 Unauthorized)


> При этом после удаления токена из базы данных в текущей реализации возможны запросы с access токеном до его истечения.