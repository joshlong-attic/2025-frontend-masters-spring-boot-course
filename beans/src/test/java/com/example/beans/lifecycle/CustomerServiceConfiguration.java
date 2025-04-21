package com.example.beans.lifecycle;

import com.example.beans.SchemaUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan
class CustomerServiceConfiguration {

	@Bean
	EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
	}

	@Bean
	JdbcClient jdbcClient(DataSource dataSource) {
		return JdbcClient.create(dataSource);
	}

}

@ExtendWith(OutputCaptureExtension.class)
@SpringJUnitConfig(classes = CustomerServiceConfiguration.class)
class CustomerServiceTest {

	@Test
	void customers(@Autowired DataSource db, @Autowired CustomerService customerService) throws Exception {
		SchemaUtils.initialize(db);
		assertEquals(2, customerService.customers().size());
	}

}