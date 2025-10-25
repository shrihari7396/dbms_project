FROM eclipse-temurin:21-jre

WORKDIR /app

COPY ./target/dbmsBackend-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

CMD ["java", "-jar", "dbmsBackend-0.0.1-SNAPSHOT.jar"]
