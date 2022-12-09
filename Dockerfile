FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY src/main/resources/logback.xml /etc/logback/
ENV CLASSPATH /etc/logback/logback.xml
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]