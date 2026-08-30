# Use Eclipse Temurin (Adoptium) — the official OpenJDK distribution for Docker
FROM eclipse-temurin:26-jdk-alpine

WORKDIR /app

# Copy the built JAR from target folder
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]