# build environment
FROM maven:3.6.2-jdk-11-slim as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline --batch-mode
COPY . .
RUN mvn package -DskipTests=true

FROM openjdk:11
WORKDIR /app
COPY --from=build /app/target/idea-*.jar idea.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "idea.jar"]
