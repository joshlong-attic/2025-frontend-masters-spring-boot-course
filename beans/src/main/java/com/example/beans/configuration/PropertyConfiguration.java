package com.example.beans.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BeansProperties.class)
class PropertyConfiguration {
}

@ConfigurationProperties(prefix = "beans")
record BeansProperties(String message) {
}