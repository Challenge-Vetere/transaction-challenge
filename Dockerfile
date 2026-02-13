# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-11 AS builder

WORKDIR /app

# Copy pom and download dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/transaction-challenge-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
