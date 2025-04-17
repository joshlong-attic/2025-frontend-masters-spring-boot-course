package bootiful;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

import static bootiful.SpringKafkaApplication.TX;

/*
 * to make this work, you need to specify the transaction id prefix, the brokerProperties, and you must specify the KafkaTemplate allow nonTransactional
 */

@SpringBootTest(properties = { "spring.kafka.producer.transaction-id-prefix=pref" })
@DirtiesContext
@EmbeddedKafka(brokerProperties = { "transaction.state.log.replication.factor=1", "transaction.state.log.min.isr=1" },
		bootstrapServersProperty = "spring.kafka.bootstrap-servers", topics = { TX })
public class KafkaTransactionTests {

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	private final TransactionTemplate transactionTemplate;

	KafkaTransactionTests(@Autowired TransactionTemplate transactionTemplate,
			@Autowired KafkaTemplate<Object, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
		this.transactionTemplate = transactionTemplate;
	}

	@Test
	void transactions(@Autowired SpringKafkaTransactionalConfiguration config) throws Exception {
		Assertions.assertThatIllegalStateException().isThrownBy(() -> send(true, "a", "b", "c"));
		Assertions.assertThat(config.counter.get()).isEqualByComparingTo(0);
		send(false, "a", "b", "c");
		Assertions.assertThat(config.latch.await(10, TimeUnit.SECONDS)).isTrue();
		Assertions.assertThat(config.counter.get()).isEqualByComparingTo(3);

	}

	private void send(boolean fail, Object... messages) {
		transactionTemplate.execute(status -> {
			for (var m : messages)
				kafkaTemplate.send(TX, m);
			Assert.state(!fail, "we should" + (fail ? "" : " not") + " fail.");
			return true;
		});
	}

}
