package com.example.beans.beanpostprocessors;

import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(OutputCaptureExtension.class)
@SpringJUnitConfig(classes = CustomerServiceConfiguration.class)
class CustomerServiceTest {

    @Test
    void customers(CapturedOutput capturedOutput, @Autowired DataSource db,
                   @Autowired CustomerService customerService) throws Exception {
        SchemaUtils.initialize(db);
        assertEquals(2, customerService.customers().size());
        assertThat(capturedOutput.getAll().trim()).contains("method customers took");
    }
}