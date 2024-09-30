#!/bin/bash

if [ "$WAIT_FOR_DATABASE" = "true" ]; then
  echo 'Waiting 20s for MYSQL server to initialize Keycloak database'
  sleep 20s
fi

echo 'Start keycloak'
/opt/keycloak/bin/kc.sh start-dev