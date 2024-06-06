FROM openjdk:17
COPY target/notification-0.0.1-SNAPSHOT.jar notification-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh ./entrypoint.sh
ENTRYPOINT [ "bash", "./entrypoint.sh" ]
