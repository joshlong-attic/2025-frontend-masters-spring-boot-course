package com.example.scalability;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.io.ByteArrayOutputStream;

class VirtualThreadsTest {

	@Test
	void virtualThreads() throws Exception {
		Runner.run(true);
	}

	@Test
	void platformThreads() throws Exception {
		Runner.run(false);
	}

}

class Runner {

	static void run(boolean vt) throws Exception {
		try (var ac = (ServletWebServerApplicationContext) new SpringApplication(ScalabilityApplication.class)
			.run("--server.port=0", "--spring.threads.virtual.enabled=" + vt);) {
			var port = ac.getWebServer().getPort();
			System.out.println("the port is " + port);
			try (var out = new ByteArrayOutputStream()) {
				var exec = Runtime.getRuntime().exec("bin/test.sh " + port);
				exec.waitFor();
				exec.getInputStream().transferTo(out);
				output(vt, out.toString());
			}
			ac.stop();
		}
	}

	private static void output(boolean vt, String body) {
		System.out.println(vt + System.lineSeparator() + System.lineSeparator() + body);
	}

}