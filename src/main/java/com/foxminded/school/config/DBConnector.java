package com.foxminded.school.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnector {

    private final String url;
    private final String user;
    private final String password;

    public DBConnector(String propertyFile) {
        ResourceBundle rb = ResourceBundle.getBundle(propertyFile);
        url = rb.getString("url");
        user = rb.getString("user");
        password = rb.getString("password");
    }

    public Connection connect () {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
