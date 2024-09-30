#!/bin/bash

if [ "$WAIT_FOR_DATABASE" = "true" ]; then
  echo 'Waiting for keycloak'
    sleep 35
fi

echo 'Starting keycloakmanagement service'
java -jar ./keycloakmanagement-0.0.1-SNAPSHOT.jar