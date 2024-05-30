FROM openjdk:17-jdk-alpine
WORKDIR /login-service
MAINTAINER ravindra
COPY target/login-0.0.1-SNAPSHOT.jar  /login-service/login-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","login-0.0.1-SNAPSHOT.jar"]
EXPOSE 8083