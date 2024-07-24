FROM openjdk:21-jre-slim

WORKDIR /app

COPY target/myapp.jar /app/myapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myapp.jar"]