# Multi-stage Dockerfile: first stage builds the project with Maven, second stage runs the jar

FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the files required to download dependencies first (speeds up cache)
COPY pom.xml mvnw ./
COPY .mvn .mvn
# Copy source
COPY src ./src

# Build the project (skip tests to speed up)
RUN mvn -B -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy the fat jar produced by the build stage. Use wildcard to avoid hardcoding the version.
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
