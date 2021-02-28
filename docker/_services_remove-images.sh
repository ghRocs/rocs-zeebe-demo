#!/bin/bash
echo "Removing all 'Zeebe Demo' images"
docker rmi $(docker images |grep zeebe-demo|awk '{print $3;}')

echo "DONE"