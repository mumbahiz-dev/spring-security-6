FROM openjdk:17-jdk-alpine
COPY "target/*.jar" spring-security-6
ENTRYPOINT ["java", "-jar", "target/spring-security-6.jar"]