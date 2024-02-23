#!/bin/bash


composeDir="."

docker-compose -f ${composeDir}/docker-compose-k6.yml run --rm k6 run /scripts/$1
# docker-k6-test.sh 스크립트 이름