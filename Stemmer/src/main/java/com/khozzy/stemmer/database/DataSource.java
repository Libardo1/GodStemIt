package com.khozzy.stemmer.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private ComboPooledDataSource cpds;
    private static DataSource datasource;

    private DataSource() throws IOException, SQLException {
        cpds = new ComboPooledDataSource();

        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/sentiment_analysis?characterEncoding=UTF-8");
        cpds.setUser("root");
        cpds.setPassword("samolot123");
        cpds.setInitialPoolSize(10);
        cpds.setAcquireIncrement(10);
        cpds.setMaxPoolSize(200);
        cpds.setMinPoolSize(10);
        cpds.setMaxStatements(200);
    }

    public static DataSource getInstance() throws IOException, SQLException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        }

        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
}
