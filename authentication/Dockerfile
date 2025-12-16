# Stage 1: Download the base image
FROM eclipse-temurin:17-jdk-jammy

#Copy authentication.jar into the image
COPY authentication/target/authentication*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://74.234.38.91:4444/salesappdb?sslmode=disable
ENV SPRING_DATASOURCE_USERNAME=salesapp
ENV SPRING_DATASOURCE_PASSWORD=on5YWFLMLUTQni7t2bR6
ENV JWT_SECRET=mySuperSecretKey1234567890abcdef9876543210

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
