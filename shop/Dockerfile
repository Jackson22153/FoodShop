FROM openjdk:17
COPY target/shop-0.0.1-SNAPSHOT.jar shop-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh ./entrypoint.sh
VOLUME [ "/usr/src/app/image/product" ]
VOLUME [ "/usr/src/app/image/category" ]
ENTRYPOINT [ "bash", "./entrypoint.sh" ]
