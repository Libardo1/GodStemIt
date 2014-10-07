package com.khozzy.stemmer.database;

import com.khozzy.stemmer.utils.Utils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {

    private ComboPooledDataSource cpds;
    private static DataSource datasource;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        Properties properties = Utils.readProperties("database.properties");

        cpds = new ComboPooledDataSource();

        cpds.setDriverClass(properties.getProperty("driverClass"));
        cpds.setJdbcUrl(properties.getProperty("jdbcUrl"));
        cpds.setUser(properties.getProperty("username"));
        cpds.setPassword(properties.getProperty("password"));
        cpds.setInitialPoolSize(Integer.valueOf(properties.getProperty("initialPoolSize")));
        cpds.setAcquireIncrement(Integer.valueOf(properties.getProperty("acquireIncrement")));
        cpds.setMaxPoolSize(Integer.valueOf(properties.getProperty("maxPoolSize")));
        cpds.setMinPoolSize(Integer.valueOf(properties.getProperty("minPoolSize")));
        cpds.setMaxStatements(Integer.valueOf(properties.getProperty("maxStatements")));
    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
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
