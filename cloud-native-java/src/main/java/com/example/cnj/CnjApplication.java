package com.example.cnj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@SpringBootApplication
public class CnjApplication {

	public static void main(String[] args) {
		SpringApplication.run(CnjApplication.class, args);
	}

	@Bean
	@ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
	Greeting kubernetes() {
		return new Greeting("Kubernetes");
	}

	@Bean
	@ConditionalOnMissingBean
	Greeting basic() {
		return new Greeting("localhost");
	}

}

record Greeting(String greeting) {
}

@Controller
@ResponseBody
class GreetingsController {

	private final String greetings;

	GreetingsController(Greeting greeting) {
		this.greetings = greeting.greeting();
	}

	@GetMapping("/greetings")
	Map<String, String> hello() {
		return Map.of("greeting", "Hello, " + this.greetings + "!");
	}

}
