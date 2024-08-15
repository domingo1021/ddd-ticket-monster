# multi-stage build, reference: https://docs.docker.com/guides/docker-concepts/building-images/multi-stage-builds/
FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
WORKDIR /opt/app
ENV SPRING_PROFILES_ACTIVE=docker
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:21.0.2_13-jre-jammy AS deployment
WORKDIR /opt/app
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=production
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]