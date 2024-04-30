package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    static Connection connection;
    static String dbHost = "jdbc:mysql://localhost:3306/";
    static String dbName = "patient-record-management-cli";
    static String dbUser = "root";
    static String dbPassword = "password123@";

    public static Connection connectToDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbHost + dbName, dbUser, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
