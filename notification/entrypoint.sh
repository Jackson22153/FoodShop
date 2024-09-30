#!/bin/bash

if [ "$WAIT_FOR_DATABASE" = "true" ]; then
    echo 'Waiting for database'
    sleep 20s
fi

echo 'Starting order service'
java -jar /notification-0.0.1-SNAPSHOT.jar