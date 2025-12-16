# Stage 1: Download the base image
FROM eclipse-temurin:17-jdk-jammy

#Copy authentication.jar into the image
COPY target/datamanagement*.jar app.jar

# Expose the port the app runs on
EXPOSE 9090

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
