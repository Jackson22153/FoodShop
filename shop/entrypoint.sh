#!/bin/bash

if [ "$WAIT_FOR_SQL" = "true" ]; then
    echo 'Waiting for database'
    sleep 20s
fi

echo 'Starting shop service'
java -jar /shop-0.0.1-SNAPSHOT.jar