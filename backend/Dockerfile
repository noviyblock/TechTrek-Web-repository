FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts gradle.properties ./
COPY app app

RUN chmod +x gradlew \
 && ./gradlew :app:bootJar -x test \
 && cp $(find app/build/libs -type f -name "*.jar" ! -name "*-plain.jar" -print -quit) app.jar

EXPOSE 8080
CMD ["java","-jar","/app/app.jar"]
