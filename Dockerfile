#FROM eclipse-temurin:17-jre-alpine
FROM openjdk:17
COPY "target/*.jar" /spring-security-6.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/spring-security-6.jar"]