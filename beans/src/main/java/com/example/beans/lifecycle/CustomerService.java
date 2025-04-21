package com.example.beans.lifecycle;

import com.example.beans.Customer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;

@Service
class CustomerService implements InitializingBean {

	private final JdbcClient db;

	CustomerService(JdbcClient db) {
		this.db = db;
	}

	Collection<Customer> customers() throws Exception {
		return db.sql("select id, name from CUSTOMER")
			.query((rs, _) -> new Customer(rs.getInt("id"), rs.getString("name")))
			.list();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.db, "the db should not be null");
	}

	@PreDestroy
	void destroy() {
		System.out.println("shutting down " + getClass().getSimpleName() + '.');
	}

}
