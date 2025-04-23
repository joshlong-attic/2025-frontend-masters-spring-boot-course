package com.example.e2e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@SpringBootApplication
public class E2eApplication {

    public static void main(String[] args) {
        SpringApplication.run(E2eApplication.class, args);
    }

}


@Controller
@ResponseBody
class CustomerController {

    private final CustomerRepository customerRepository;

    CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers")
    List<Customer> customers() {
        return customerRepository.findAll();
    }
}

record Customer(@Id int id, String name) {
}

interface CustomerRepository extends
        ListCrudRepository<Customer, Integer> {
}