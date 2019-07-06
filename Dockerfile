# build environment
FROM maven:3.5-jdk-10 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline --batch-mode
COPY . .
RUN mvn package -DskipTests=true

FROM openjdk:10
WORKDIR /app
COPY --from=build /app/target/idea-1.0.0.jar idea.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "idea.jar"]
