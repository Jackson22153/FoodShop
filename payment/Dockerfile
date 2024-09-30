FROM openjdk:17
COPY /target/payment-0.0.1-SNAPSHOT.jar .
COPY /entrypoint.sh .
ENTRYPOINT [ "bash", "entrypoint.sh" ]