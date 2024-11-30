# Stage 1: Build
FROM maven:3.9.5-amazoncorretto-21 AS builder

LABEL description="Build stage for the entry-gate microservice"

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:21-alpine

LABEL description="Runtime image for the entry-gate microservice"

EXPOSE 9080

# Copy the JAR file from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app.jar"]