# Stage 1: Download the base image
FROM eclipse-temurin:17-jdk-jammy

#Copy datamanagement.jar into the image
COPY datamanagement/target/datamanagement*.jar app.jar

# Expose the port the app runs on
EXPOSE 9090

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
