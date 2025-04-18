package com.example.beans.raii;

import com.example.beans.Customer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

class CustomerService {

    final DataSource db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();


    Collection<Customer> customers() throws Exception {
        var customers = new ArrayList<Customer>();
        var sql = "SELECT id, name FROM customer";
        try (var conn = this.db.getConnection();//
             var statement = conn.prepareStatement(sql);//
             var rs = statement.executeQuery()) {
            while (rs.next()) {
                var id = rs.getInt("id");
                var name = rs.getString("name");
                customers.add(new Customer(id, name));
            }
        }
        return customers;
    }
}
