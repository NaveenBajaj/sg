package com.sg.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public MySqlConnection(String jdbcURL, String jdbcUsername, String jdbcPassword){
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    public Connection getConnection() {
        try {
            if (jdbcConnection == null || jdbcConnection.isClosed()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
                jdbcConnection = DriverManager.getConnection(
                        jdbcURL, jdbcUsername, jdbcPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jdbcConnection;
    }

    public void closeConnection() {
        try {
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
