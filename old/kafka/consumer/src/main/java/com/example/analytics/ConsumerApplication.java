package com.example.analytics;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	Function<KStream<String, PageView>, KStream<String, Long>> processor() {
		return kStream -> kStream//
			.filter((k, pve) -> pve.duration() > 0)//
			.map((k, pve) -> new KeyValue<>(pve.page(), 0L))//
			.groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))//
			.count(Materialized.as("pcmv"))//
			.toStream();
	}

	@Bean
	Consumer<KTable<String, Long>> pageCount() {
		return counts -> counts.toStream().foreach((key, value) -> System.out.println(key + "=" + value));
	}

}

record PageView(String userId, String page, long duration) {
}