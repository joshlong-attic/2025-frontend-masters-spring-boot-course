package bootiful;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static bootiful.SpringKafkaApplication.*;
import static bootiful.SpringKafkaApplication.GREETINGS;
import static bootiful.SpringKafkaApplication.NOTIFICATIONS;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers", topics = { GREETINGS, NOTIFICATIONS })
class SpringKafkaApplicationTests {

	private final AtomicInteger counter;

	SpringKafkaApplicationTests(@Autowired SpringKafkaConfiguration springKafkaApplication) {
		this.counter = springKafkaApplication.counter;
	}

	@Test
	void counter() throws Exception {
		Thread.sleep(5_000);
		Assertions.assertTrue(this.counter.get() > 0);
	}

	@Test
	void topics(@Autowired EmbeddedKafkaBroker broker) {
		for (var topic : List.of(GREETINGS, NOTIFICATIONS)) {
			Assertions.assertTrue(broker.getTopics().contains(topic));
		}
	}

}
