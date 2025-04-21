package com.example.deskcheck;

import org.springframework.boot.SpringApplication;

public class TestDeskcheckApplication {

	public static void main(String[] args) {
		SpringApplication.from(DeskcheckApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
