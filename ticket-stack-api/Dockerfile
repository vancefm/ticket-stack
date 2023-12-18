FROM openjdk:17-alpine

EXPOSE 8080

COPY target/ticket-stack-api-0.0.1-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/logback-spring.xml /app/

WORKDIR /app

ENTRYPOINT ["java","-cp","app/app.jar:/app","-Dlogging.config=/app/logback-spring.xml","-jar","/app/app.jar"]
RUN ls -la /app