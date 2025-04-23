package com.example.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.SocatContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class AiApplicationTest {

    public static void main(String[] args) {
        SpringApplication.from(AiApplicationTest::main)
                .with(AiApplicationTest.class)
                .run(args);
    }

    @Bean
    SocatContainer socat() {
        return new SocatContainer(DockerImageName.parse("alpine/socat:1.8.0.1"))
                .withTarget(80, "model-runner.docker.internal");
    }

    @Bean
    DynamicPropertyRegistrar properties(SocatContainer socat) {
        return (registrar) -> {
            registrar.add("spring.ai.openai.base-url", () -> "http://%s:%d/engines".formatted(socat.getHost(), socat.getMappedPort(80)));
            registrar.add("spring.ai.openai.api-key", () -> "test-api-key");
            registrar.add("spring.ai.openai.chat.options.model", () -> "ai/gemma3");
        };
    }
}
