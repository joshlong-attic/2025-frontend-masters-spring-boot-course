package com.example.beans.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CustomerServiceTestConfiguration {

	@Bean
	static LoggingBeanPostProcessor loggingBeanPostProcessor() {
		return new LoggingBeanPostProcessor();
	}

}
