package com.example.dop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DopApplication {

	public static void main(String[] args) {
		SpringApplication.run(DopApplication.class, args);
	}

}

sealed interface Loan permits UnsecuredLoan, SecuredLoan {

}

record UnsecuredLoan(float interest) implements Loan {
}

final class SecuredLoan implements Loan {

}

@Component
class Loans {

	String displayMessageFor(Loan loan) {
		return switch (loan) {
			case UnsecuredLoan unsecuredLoan -> "Unsecured Loan with interest of " + unsecuredLoan.interest();
			case SecuredLoan _ -> "Secured Loan";
		};
	}

}