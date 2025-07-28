# Enterprise Java with Spring Boot Course
This is a companion repository for the [Enterprise Java with Spring Boot](https://frontendmasters.com/courses/spring-boot/) course on Frontend Masters.
[![Frontend Masters](https://static.frontendmasters.com/assets/brand/logos/full.png)](https://frontendmasters.com/courses/spring-boot/)

## Course Setup

The repo contains several projects demonstrating the capabilities of Spring Boot. Follow the setup instructions below to ensure you have the correct software and tools to follow along with the course.

### Install SDKMAN and Java 24
- https://sdkman.io/
- `curl -s "https://get.sdkman.io" | bash`
- `sdk install java 24-graalce`
- `sdk default java 24-graalce`


## Install & Run Docker
- Install [Docker Desktop](https://www.docker.com/)


## Clone the Repo and Run the Intro App
Josh "speed runs" the creation of an application at the beginning of the course. The `intro` directory is a version of this application. You can use it to test your setup and ensure you have all the requirements:

```bash
git clone https://github.com/joshlong-attic/2025-frontend-masters-spring-boot-course
cd 2025-frontend-masters-spring-boot-course/intro/demo
docker compose up
./mvnw spring-boot:run
```

Open localhost:8080/hello in a browser to test the application

