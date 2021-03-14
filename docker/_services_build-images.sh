#!/bin/bash
echo "Building image for Spring Boot Zeebe Demo Services"
cd ../services/spring-boot/service-base
mvn clean install
cd ..
mvn clean spring-boot:build-image

echo "Building image for Spring Boot With Starter Zeebe Demo Services"
cd ../spring-boot-with-starter/service-common
mvn clean install
cd ..
mvn clean spring-boot:build-image

echo "DONE"