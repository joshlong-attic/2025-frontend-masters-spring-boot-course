package com.example.beans.raii;

import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerServiceTest {

	@Test
	void customers() throws Exception {
		var customerService = new CustomerService();
		SchemaUtils.initialize(customerService.db);
		assertEquals(2, customerService.customers().size());
	}

}