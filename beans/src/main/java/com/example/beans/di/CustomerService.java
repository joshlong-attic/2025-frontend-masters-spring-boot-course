package com.example.beans.di;

import com.example.beans.Customer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

class CustomerService {

	private final DataSource db;

	CustomerService(DataSource db) {
		this.db = db;
	}

	Collection<Customer> customers() throws Exception {
		var customers = new ArrayList<Customer>();
		var sql = "SELECT id, name FROM customer";
		try (var conn = this.db.getConnection(); //
				var statement = conn.prepareStatement(sql); //
				var rs = statement.executeQuery()) {
			while (rs.next()) {
				var id = rs.getInt("id");
				var name = rs.getString("name");
				customers.add(new Customer(id, name));
			}
		}
		return customers;
	}

}
