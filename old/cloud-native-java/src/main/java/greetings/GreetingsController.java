package greetings;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
public class GreetingsController {

	private final String name;

	public GreetingsController(String name) {
		this.name = name;
	}

	@GetMapping("/greetings")
	Map<String, String> hello() {
		return Map.of("greeting", "Hello, " + this.name + "!");
	}

}
