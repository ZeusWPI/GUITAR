## Build the project
# see https://blog.aadilp.com/containerize-an-application-with-podman-and-buildah
FROM docker.io/gradle:jdk24 AS builder

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle
COPY src /app/src

RUN gradle clean build -x test

## Build container to run the project
# see https://spring.io/guides/gs/spring-boot-docker
FROM docker.io/eclipse-temurin:24-jre

RUN useradd -d /app guitar
USER guitar:guitar

WORKDIR /app

## Replace springpodman below with your project name
COPY --from=builder /app/build/libs/GUITAR*.jar service.jar

EXPOSE 8080

CMD ["java", "-jar", "service.jar"]
