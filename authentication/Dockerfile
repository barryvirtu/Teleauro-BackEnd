# Stage 1: Build the JAR using Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Copy source code
COPY src src

# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the built JAR from stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
