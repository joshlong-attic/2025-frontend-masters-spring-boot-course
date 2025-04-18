package com.example.beans.portableserviceabstractions;

import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerServiceTest {

    @Test
    void customers() throws Exception {
        var db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        var jdbcClient = JdbcClient.create(db);
        SchemaUtils.initialize(db);
        var customerService = new CustomerService(jdbcClient);
        assertEquals(2, customerService.customers().size());
    }
}