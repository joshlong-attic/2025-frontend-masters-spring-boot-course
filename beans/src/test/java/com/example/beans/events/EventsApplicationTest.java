package com.example.beans.events;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventsApplicationTest {

	@Test
	void events(@Autowired EventsApplication eventsApplication) {
		Assertions.assertThat(eventsApplication.counter()).isEqualTo(3);
	}

}