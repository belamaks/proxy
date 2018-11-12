FROM openjdk:8-alpine

ADD rep/app.jar /app/app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]