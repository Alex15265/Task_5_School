package com.foxminded.school.config;

import com.foxminded.school.config.DBConnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLScriptConverter {
    private final Connection connection;

    public SQLScriptConverter(DBConnector connector) {
        connection = connector.connect();
    }

    public void convert(File file) {
        StringBuilder builder = new StringBuilder();
        if (file.length() == 0) {
            throw new IllegalArgumentException("File is empty");
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String string;
            while ((string = reader.readLine()) != null) {
                builder.append(string);
            }
        } catch(IOException ex){
            throw new IllegalArgumentException("File not found");
        }

        try (Statement statement = connection.createStatement()) {

            String[] scriptLines = builder.toString().split(";");

            for (String scriptLine : scriptLines) {
                if (!scriptLine.trim().equals("")) {
                    statement.executeUpdate(scriptLine);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
