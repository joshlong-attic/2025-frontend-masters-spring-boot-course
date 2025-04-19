package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}

@Controller
@ResponseBody
class GreetingController {

    private final AtomicInteger port = new AtomicInteger(0);

    @EventListener
    void onStartup(WebServerInitializedEvent event) {
        this.port.set(event.getWebServer().getPort());
    }

    @GetMapping("/hello")
    Map<String, String> hello() {
        return Map.of("message", "Hello, from localhost:" + this.port.get() + "!");
    }
}