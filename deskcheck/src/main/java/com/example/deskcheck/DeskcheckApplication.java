package com.example.deskcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;
import java.util.Objects;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class DeskcheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeskcheckApplication.class, args);
	}

	// sources the value from .envrc file
	@Bean
	RouterFunction<ServerResponse> routerFunction(@Value("${my.key}") String key, Environment environment) {
		return route()//
			.GET("/hello", _ -> ok().body(Map.of("message", "hello, Frontend Masters!!")))//
			.GET("/value", _ -> ok().body(Map.of("message", key)))//
			.GET("/env", _ -> ok().body(Map.of("message", Objects.requireNonNull(environment.getProperty("my.key")))))//
			.build();
	}

}
