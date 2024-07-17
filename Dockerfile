FROM openjdk:17-jdk-slim
COPY target/cond-way-0.0.1.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]