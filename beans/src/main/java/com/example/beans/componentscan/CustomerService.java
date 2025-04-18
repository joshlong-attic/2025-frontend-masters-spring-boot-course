package com.example.beans.componentscan;

import com.example.beans.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
class CustomerService {

    private final JdbcClient db;

    CustomerService(JdbcClient db) {
        this.db = db;
    }

    Collection<Customer> customers() throws Exception {
        return db
                .sql("select id, name from CUSTOMER")
                .query((rs, _) -> new Customer(rs.getInt("id"), rs.getString("name")))
                .list();
    }

}

