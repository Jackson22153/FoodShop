FROM quay.io/keycloak/keycloak:23.0.7 as builder
ADD --chown=keycloak:keycloak customkeycloakprovider-1.0-SNAPSHOT.jar /opt/keycloak/providers/customkeycloakprovider-1.0-SNAPSHOT.jar
ADD --chown=keycloak:keycloak keycloak-bcrypt-1.6.0.jar /opt/keycloak/providers/keycloak-bcrypt-1.6.0.jar

COPY keycloak.conf /opt/keycloak/conf/keycloak.conf

COPY keycloak-server.crt.pem /opt/keycloak/conf/keycloak-server.crt.pem
COPY keycloak-server.key.pem /opt/keycloak/conf/keycloak-server.key.pem

COPY /entrypoint.sh ./entrypoint.sh

RUN /opt/keycloak/bin/kc.sh build 

ENTRYPOINT ./entrypoint.sh