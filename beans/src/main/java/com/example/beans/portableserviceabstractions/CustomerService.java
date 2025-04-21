package com.example.beans.portableserviceabstractions;

import com.example.beans.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.Collection;

class CustomerService {

	private final JdbcClient db;

	CustomerService(JdbcClient db) {
		this.db = db;
	}

	Collection<Customer> customers() throws Exception {
		return db.sql("select id, name from CUSTOMER")
			.query((rs, _) -> new Customer(rs.getInt("id"), rs.getString("name")))
			.list();

	}

}
