FROM openjdk:17-jdk-alpine
COPY "target/*.jar" spring-security-6
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/spring-security-6.jar"]