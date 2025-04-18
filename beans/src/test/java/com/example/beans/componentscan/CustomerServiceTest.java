package com.example.beans.componentscan;

import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringJUnitConfig(classes = CustomerServiceConfiguration.class)
class CustomerServiceTest {

    @Test
    void customers(
            @Autowired DataSource db,
            @Autowired CustomerService customerService) throws Exception {
        SchemaUtils.initialize(db);
        assertEquals(2, customerService.customers().size());

    }
}