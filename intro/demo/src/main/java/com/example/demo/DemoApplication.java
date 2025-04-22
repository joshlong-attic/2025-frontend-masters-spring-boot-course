package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> customerRoutes(CustomerRepository repository) {
        return route()
                .GET("/customers", _ -> ok().body(repository.findAll()))
                .GET("/hello", _ -> ok().body(Map.of("message", "Hello World!!")))
                .build();
    }
}


interface CustomerRepository extends ListCrudRepository<Customer, Integer> {
}

record Customer(@Id int id, String name) {
}