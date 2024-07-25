# Use uma imagem base do JDK
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o jar do aplicativo
COPY target/account-0.0.1-SNAPSHOT.jar /app/account.jar

# Defina a variável de ambiente JAVA_HOME
ENV JAVA_HOME /usr/local/openjdk-17

# Exponha a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "account.jar"]