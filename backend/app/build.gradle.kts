plugins {
    // Подключаем плагины для Spring Boot и управления зависимостями
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    java
}

group = "com.startupgame"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    // Используем Maven Central для загрузки зависимостей
    mavenCentral()
}

dependencies {
    // Spring Boot Starter Web (для создания REST API)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Boot Starter Data JPA (для работы с базой данных)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // PostgreSQL драйвер (для подключения к базе данных)
    implementation("org.postgresql:postgresql")

    // Spring Boot Starter Security (для аутентификации и авторизации)
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Spring Boot Starter Test (для тестирования)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // JUnit Jupiter (для unit-тестов)
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
    // Используем JUnit Platform для тестов
    useJUnitPlatform()
}