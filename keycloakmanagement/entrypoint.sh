#!/bin/bash

if [ "$WAIT_FOR_SQL" = "true" ]; then
  echo 'Waiting for keycloak'
    sleep 35
fi

echo 'Starting keycloakmanagement service'
java -jar ./keycloakmanagement-0.0.1-SNAPSHOT.jar