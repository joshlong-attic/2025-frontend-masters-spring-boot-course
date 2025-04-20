package com.example.beans.configuration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class PropertyConfigurationTest {

	@Test
	void contextLoads(@Autowired Environment environment, @Value("${beans.message}") String message,
			@Autowired BeansProperties configuration) {
		var constant = "hello world";
		Assertions.assertThat(environment.getProperty("beans.message")).isEqualTo(constant);
		Assertions.assertThat(message).isEqualTo(constant);
		Assertions.assertThat(configuration.message()).isEqualTo(constant);
	}

}