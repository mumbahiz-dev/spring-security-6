FROM openjdk:17-jdk-alpine
WORKDIR /target
ARG JAR_FILE=target/
COPY spring-security-6.jar ${JAR_FILE}
ENTRYPOINT ["java", "-jar", "target/spring-security-6.jar"]