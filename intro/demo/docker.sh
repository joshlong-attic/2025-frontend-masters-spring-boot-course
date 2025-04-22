#!/usr/bin/env bash 

./mvnw spring-boot:build-image 

docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal/mydatabase docker.io/library/demo:0.0.1-SNAPSHOT 
