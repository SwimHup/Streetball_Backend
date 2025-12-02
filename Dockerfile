FROM gradle:jdk17-jammy AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build --no-daemon

LABEL org.name="streetball"

FROM eclipse-temurin:17-jdk-jammy

COPY --from=build /home/gradle/src/build/libs/streetball_backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

