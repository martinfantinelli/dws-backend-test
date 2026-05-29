# syntax=docker/dockerfile:1

# ---- Build stage: compile and package the jar ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies first (only re-runs when pom.xml changes)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Extract stage: split the jar into Spring Boot layers for better caching ----
FROM eclipse-temurin:21-jre AS extract
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract --destination extracted

# ---- Runtime stage: slim JRE, non-root, layered copy ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Run as an unprivileged user
RUN groupadd --system spring && useradd --system --gid spring spring

# Copy layers most-stable-first so image rebuilds reuse cached layers
COPY --from=extract --chown=spring:spring /app/extracted/dependencies/ ./
COPY --from=extract --chown=spring:spring /app/extracted/spring-boot-loader/ ./
COPY --from=extract --chown=spring:spring /app/extracted/snapshot-dependencies/ ./
COPY --from=extract --chown=spring:spring /app/extracted/application/ ./

USER spring:spring
EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
