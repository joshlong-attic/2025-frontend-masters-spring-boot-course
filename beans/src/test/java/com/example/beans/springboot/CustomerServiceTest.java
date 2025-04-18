package com.example.beans.springboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = BeansApplication.class)
class CustomerServiceTest {

    @Test
    void customers(CapturedOutput capturedOutput,
                   @Autowired CustomerService customerService) throws Exception {
        assertEquals(2, customerService.customers().size());
        assertThat(capturedOutput.getAll().trim()).contains("method customers took");
    }
}

