package org.generalka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static UserDAO getUserDAO() {
        return new UserDAOImpl();
        // If you have other DAOs, you can create them here and return instances accordingly.
    }
}
