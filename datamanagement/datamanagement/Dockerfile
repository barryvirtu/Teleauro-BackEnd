FROM eclipse-temurin:21-jdk
VOLUME /tmp
COPY target/datamanagement-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/osopenuprn_202509_cut.csv /app/osopenuprn_202509_cut.csv
ENTRYPOINT ["java","-jar","/app.jar"]
