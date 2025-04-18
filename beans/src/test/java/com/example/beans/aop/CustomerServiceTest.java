package com.example.beans.aop;

import com.example.beans.LoggableProxyMaker;
import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(OutputCaptureExtension.class)
class CustomerServiceTest {

    @Test
    void customers(CapturedOutput capturedOutput) throws Exception {
        var db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        var jdbcClient = JdbcClient.create(db);
        SchemaUtils.initialize(db);
        var customerService = LoggableProxyMaker.proxy(new CustomerService(jdbcClient));
        assertEquals(2, customerService.customers().size());
        assertThat(capturedOutput.getAll().trim()).contains("method customers took");
    }
}
