package com.example.dop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoansTest {

    @Test
    void message() {
        var loans = new Loans();
        assertEquals("Secured Loan", loans.displayMessageFor(new SecuredLoan()));
        assertEquals("Unsecured Loan with interest of " +   5.0f,
                loans.displayMessageFor(new UnsecuredLoan(5.0f)));
    }
}