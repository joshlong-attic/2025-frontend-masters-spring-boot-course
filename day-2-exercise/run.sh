#!/usr/bin/env bash

# make sure to export the port in `compose.yml`! 
# '5432' should be '5432:5432' 
# then docker compose down
# then docker compose up -d 

# if you put this in a script, say `run.sh`, remember to : chmod a+x run.sh

# ./mvnw -DkipTests -Pnative spring-boot:build-image


export SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal/mydatabase
export SPRING_DATASOURCE_USERNAME=myuser
export SPRING_DATASOURCE_PASSWORD=secret

# be sure to replace the final Docker image with the right name
docker run \
	-e SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD  \
	-e SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
	-e SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME  \
	-p 8080:8080 \
	docker.io/library/e2e:0.0.1-SNAPSHOT

# ^ - docker image name there!		

