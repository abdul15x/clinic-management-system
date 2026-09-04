# Stage 1: Build with Java 26
FROM eclipse-temurin:26-jdk-alpine AS builder
WORKDIR /app

# Install Maven 3.9.6 manually (official maven images don't have Java 26 yet)
RUN wget -q https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xzf apache-maven-3.9.6-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/bin/mvn && \
    rm apache-maven-3.9.6-bin.tar.gz

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Run with Java 26 JRE
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]