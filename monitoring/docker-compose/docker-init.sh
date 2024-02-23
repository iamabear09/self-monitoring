#!/bin/bash

# 로컬에서 모니터링 환경과 부하 테스트 환경 설정을 위해 한번에 필요한 Container Up

composeDir="."

# mysql up
docker-compose -f ${composeDir}/docker-compose-db.yml up -d
# monitoring system
docker-compose -f ${composeDir}/docker-compose-monitoring.yml up -d
# scouter
docker-compose -f ${composeDir}/docker-compose-scouter.yml up -d
# load test
docker-compose -f ${composeDir}/docker-compose-k6.yml up influxdb test-dashboard -d

