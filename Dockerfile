# Build stage
FROM gradle:8.11-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN gradle bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

RUN mkdir -p /app/upload

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
