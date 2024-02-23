#!/bin/bash

composeDir="."

# mysql up
docker-compose -f ${composeDir}/docker-compose-db.yml up -d
# monitoring system
docker-compose -f ${composeDir}/docker-compose-monitoring.yml up -d
# scouter
docker-compose -f ${composeDir}/docker-compose-scouter.yml up -d
# load test
docker-compose -f ${composeDir}/docker-compose-k6.yml up influxdb test-dashboard -d


