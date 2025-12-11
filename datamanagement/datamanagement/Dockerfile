# Stage 1: Build the JAR
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY mvnw* pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/osopenuprn_202509_cut.csv /app/osopenuprn_202509_cut.csv

EXPOSE 9090

ENTRYPOINT ["java","-jar","app.jar"]
