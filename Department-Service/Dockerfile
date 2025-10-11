FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/department-service-0.0.1-SNAPSHOT.jar department-service.jar
ENTRYPOINT ["java", "-jar", "department-service.jar"]