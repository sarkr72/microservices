FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]