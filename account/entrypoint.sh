#!/bin/bash

if [ "$WAIT_FOR_SQL" = "true" ]; then
    echo 'Waiting for database'
    sleep 20s
fi

echo 'Starting account service'
java -jar /account-0.0.1-SNAPSHOT.jar