#!/bin/bash
echo "Building image for Spring Boot Zeebe Demo Services"
cd ../services/spring-boot/service-base
mvn clean install
cd ..
mvn clean spring-boot:build-image

echo "DONE"