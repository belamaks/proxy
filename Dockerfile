FROM openjdk:8-alpine

ADD rep/demonstration_server*SNAPSHOT.jar /app/app.jar

EXPOSE 9999

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]