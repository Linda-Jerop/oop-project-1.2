package com.school.oop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DatabaseConnection opens a connection to MySQL.
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/strathmore_rooms?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "stratizen";
    private static final String PASSWORD = "stratizen_dev";

    // This method opens a new connection. The code that asks for it must close it when it is done.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
