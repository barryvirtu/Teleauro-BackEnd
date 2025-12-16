# Stage 1: Download the base image
FROM eclipse-temurin:17-jdk-jammy

#Copy datamanagement.jar into the image
COPY datamanagement/target/datamanagement*.jar app.jar

# Expose the port the app runs on
EXPOSE 9090

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://74.234.38.91:4444/salesappdb?sslmode=disable
ENV SPRING_DATASOURCE_USERNAME=salesapp
ENV SPRING_DATASOURCE_PASSWORD=on5YWFLMLUTQni7t2bR6

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
