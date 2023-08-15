#FROM eclipse-temurin:17-jre-alpine
FROM eclipse-temurin:17-jre-alpine
COPY "target/*.jar" /spring-security-6.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/spring-security-6.jar"]