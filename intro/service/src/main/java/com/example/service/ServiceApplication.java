package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> myRoutes(CustomerRepository repository) {
		return route()//
			.GET("/customers", _ -> ok().body(repository.findAll()))//
			.GET("/hello", _ -> ok().body(Map.of("message", "Hello World")))//
			.build();
	}

}

record Customer(int id, String name) {
}

interface CustomerRepository extends ListCrudRepository<Customer, Integer> {

}