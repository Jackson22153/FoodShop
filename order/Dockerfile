FROM openjdk:17
COPY target/order-0.0.1-SNAPSHOT.jar order-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh ./entrypoint.sh
ENTRYPOINT [ "bash", "./entrypoint.sh" ]
