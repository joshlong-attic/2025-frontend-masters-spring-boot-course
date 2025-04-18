package com.example.beans;

import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;

public abstract class SchemaUtils {

    public static void initialize(DataSource dataSource) throws Exception {
        var databaseInitializationSettings = new DatabaseInitializationSettings();
        databaseInitializationSettings.setSchemaLocations(List.of("schema.sql", "data.sql"));
        var sqlInit = new SqlDataSourceScriptDatabaseInitializer(dataSource,
                databaseInitializationSettings);
        sqlInit.afterPropertiesSet();
        Assert.state(sqlInit.initializeDatabase(), "the sql db coulnd't be initialized");
    }
}
