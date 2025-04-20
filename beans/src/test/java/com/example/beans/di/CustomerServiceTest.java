package com.example.beans.di;

import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerServiceTest {

	@Test
	void customers() throws Exception {
		var db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		SchemaUtils.initialize(db);
		var customerService = new CustomerService(db);
		assertEquals(2, customerService.customers().size());
	}

}