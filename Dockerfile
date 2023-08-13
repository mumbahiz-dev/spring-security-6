FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/
COPY ${JAR_FILE} spring-security-6.jar
ENTRYPOINT ["java", "-jar", "/spring-security-6.jar"]