FROM openjdk:17
COPY target/order-0.0.1-SNAPSHOT.jar order-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh ./entrypoint.sh
CMD [ "bash", "./entrypoint.sh" ]
# ENTRYPOINT ["java","-jar","/order-0.0.1-SNAPSHOT.jar"]
