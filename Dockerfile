# =========================
# 1️⃣ Build Stage
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build

#Set working Directory
WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .

# Download dependencies (cache layer)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build project
RUN mvn clean package -DskipTests

# =========================
# 2️⃣ Runtime Stage
# =========================
FROM openjdk:17-jdk-slim AS runtime

# Set working directly
WORKDIR /app

# Install MySQL client (no netcat needed)
RUN apt-get update && apt-get install -y default-mysql-client && rm -rf /var/lib/apt/lists/*

# To wait this container for host service to be ready
COPY docker/wait-for.sh /scripts/wait-for.sh
RUN chmod +x /scripts/wait-for.sh

# Copy jar from build stage
COPY --from=build /app/target/spring-boot-banking-app.jar app.jar

# Expose port
EXPOSE 8080

# Run Jar file
ENTRYPOINT ["/scripts/wait-for.sh", "mysql-banking:3306", "java", "-jar", "app.jar"]
