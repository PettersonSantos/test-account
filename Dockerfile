FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY target/account-0.0.1-SNAPSHOT.jar /app/myapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myapp.jar"]