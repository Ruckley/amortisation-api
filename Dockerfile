FROM openjdk:17-jdk-slim



WORKDIR /app

COPY target/amortisation-api-1.0-SNAPSHOT.jar /app/amortisation-api-1.0-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "amortisation-api-1.0-SNAPSHOT.jar"]