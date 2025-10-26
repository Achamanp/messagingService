FROM openjdk:21

VOLUME /tmp
VOLUME /logs

WORKDIR /app
COPY ./target/dd.messaging-system-0.0.1-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "dd.messaging-system-0.0.1-SNAPSHOT.jar"]
