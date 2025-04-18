package com.example.scalability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class ScalabilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScalabilityApplication.class, args);
    }

}

@Controller
@ResponseBody
class CoraIberkleidController {

    private final RestClient http;

    CoraIberkleidController(RestClient.Builder http) {
        this.http = http.build();
    }

    @GetMapping("/delay")
    String delay() {
        var log = "";
        log+=Thread.currentThread()   +":";
        var response = http
                .get()
                .uri("http://localhost:9000/delay/5")
                .retrieve()
                .body(String.class);
        log+= Thread.currentThread() + System.lineSeparator()  ;
        System.out.println(log);
        return response ;
    }
}