package com.example.beans.springframework;

import com.example.beans.LoggableProxyMaker;
import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Configuration
class CustomerServiceConfiguration {

    @Bean
    EmbeddedDatabase embeddedDatabaseBuilder() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    JdbcClient jdbcClient(EmbeddedDatabase embeddedDatabase) {
        return JdbcClient.create(embeddedDatabase);
    }

    @Bean
    CustomerService customerService(JdbcClient db) {
        return LoggableProxyMaker.proxy(new CustomerService(db));
    }

}


class ManualCustomerServiceTest {

    private final ApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(CustomerServiceConfiguration.class);

    @Test
    void customers() throws Exception {
        var customerService = applicationContext.getBean(CustomerService.class);
        var db = applicationContext.getBean(DataSource.class);
        SchemaUtils.initialize(db);
        assertEquals(2, customerService.customers().size());
    }
}

@ExtendWith( OutputCaptureExtension.class)
@SpringJUnitConfig(classes = CustomerServiceConfiguration.class)
class SpringTestCustomerServiceTest {

    @Test
    void customers(CapturedOutput capturedOutput , @Autowired DataSource db, @Autowired CustomerService customerService) throws Exception {
        SchemaUtils.initialize(db);
        assertEquals(2, customerService.customers().size());
        assertThat(capturedOutput.getAll().trim()).contains("method customers took");
    }
}
