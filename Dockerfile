FROM openjdk:10
EXPOSE 8080
ADD target/idea-1.0.0.jar idea.jar
ENTRYPOINT ["java", "-jar", "idea.jar"]
