# --- Stage 1: Build the Application ---
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy configuration files first to cache dependencies
COPY pom.xml .
# Download dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Create the Lightweight Runtime Image ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy the JAR from the builder stage
# Adjust the wildcard if you have specific naming conventions, but standard Spring Boot builds result in one executable jar
COPY --from=builder /app/target/*.jar app.jar

# Configuration
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

# Launch
ENTRYPOINT ["java", "-jar", "app.jar"]