#!/bin/bash
DOCKER_COMPOSE_FILE="rocs-zeebe-demo/docker-compose-ssb.yml"
OPERATE_HOST_PORT="localhost:8080"

if [ -z "$1" ]
then
  echo -e "Usage: \n${0##*/} start \n${0##*/} stop \n${0##*/} info"
  exit 1
fi

if [ $1 == start ]
then
  until [ "0"$(curl -s -I $OPERATE_HOST_PORT | awk '{print $2}' | sed -n '1p') -eq 200 ]
  do
    echo "Waiting for the Zeebe(ZEO) to be available..."
    sleep 1
  done
  docker-compose -f $DOCKER_COMPOSE_FILE up -d
  STATUS=$?
  if [ $STATUS -eq 0 ]
  then
    echo -e "\nContainers successfully started in background \nDemo Business APIs on http://localhost:18080/swagger-ui/ \nFor log info: ${0##*/} info"
  else
    echo -e "\nFailed starting containers"
  fi
elif [ $1 == stop ]
then
  docker-compose -f $DOCKER_COMPOSE_FILE down
  STATUS=$?
  if [ $STATUS -eq 0 ]
  then
    echo -e "\nContainers successfully stopped"
  else
    echo -e "\nFailed stopping containers"
  fi
elif [ $1 == info ]
then
  docker-compose -f $DOCKER_COMPOSE_FILE logs --follow
else
  echo -e "Usage: \n${0##*/} start \n${0##*/} stop \n${0##*/} info"
  exit 1
fi