# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.4/maven-plugin/build-image.html)
* [Spring Integration AMQP Module Reference Guide](https://docs.spring.io/spring-integration/reference/amqp.html)
* [Spring Integration Test Module Reference Guide](https://docs.spring.io/spring-integration/reference/testing.html)
* [Spring Integration Apache Kafka Module Reference Guide](https://docs.spring.io/spring-integration/reference/kafka.html)
* [Spring Integration HTTP Module Reference Guide](https://docs.spring.io/spring-integration/reference/http.html)
* [Spring Integration](https://docs.spring.io/spring-boot/3.4.4/reference/messaging/spring-integration.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.4/reference/web/servlet.html)
* [Spring for Apache Pulsar](https://docs.spring.io/spring-boot/3.4.4/reference/messaging/pulsar.html)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/3.4.4/reference/messaging/kafka.html)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/3.4.4/reference/messaging/amqp.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.4.4/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.4/reference/actuator/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Integrating Data](https://spring.io/guides/gs/integration/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* pulsar: [`apachepulsar/pulsar:latest`](https://hub.docker.com/r/apachepulsar/pulsar)
* rabbitmq: [`rabbitmq:latest`](https://hub.docker.com/_/rabbitmq)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

