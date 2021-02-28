#!/bin/bash
DOCKER_COMPOSE_FILE="rocs-zeebe-demo/docker-compose-zeo.yml"

if [ -z "$1" ]
then
  echo -e "Usage: \n${0##*/} start \n${0##*/} stop \n${0##*/} info"
  exit 1
fi

if [ $1 == start ]
then
  docker-compose -f $DOCKER_COMPOSE_FILE up -d
  STATUS=$?
  if [ $STATUS -eq 0 ]
  then
    echo -e "\nContainers successfully started in background \nOperate on http://localhost:8080  (demo/demo) \nFor log info: ${0##*/} info"
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