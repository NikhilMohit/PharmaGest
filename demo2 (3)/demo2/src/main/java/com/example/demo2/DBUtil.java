package com.example.demo2;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;


import java.sql.*;

public class DBUtil {
    // Declare JDBC Driver
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    // Connection
    private static Connection conn = null;

    // Connection String
    // Format: jdbc:postgresql://host:port/database
    private static final String connStr = "jdbc:postgresql://localhost:5432/Pharmagest";
    private static final String username = "postgres";
    private static final String password = "Nmohit03042004@";

    // Connect to DB
    public static Connection dbConnect() throws SQLException, ClassNotFoundException {
        // Setting PostgreSQL JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        // Establish the PostgreSQL Connection using Connection String
        try {
            conn = DriverManager.getConnection(connStr, username, password);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }

        return conn;
    }

    // Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        // Declare statement, resultSet, and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;
        try {
            // Connect to DB (Establish PostgreSQL Connection)
            dbConnect();
            System.out.println("Select statement: " + queryStmt + "\n");

            // Create statement
            stmt = conn.createStatement();

            // Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            // CachedRowSet Implementation
            // In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            // We are using CachedRowSet
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                // Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                // Close Statement
                stmt.close();
            }
            // Close connection
            dbDisconnect();
        }
        // Return CachedRowSet
        return crs;
    }


    // DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        // Declare statement as null
        Statement stmt = null;
        try {
            // Connect to DB (Establish PostgreSQL Connection)
            dbConnect();
            // Create Statement
            stmt = conn.createStatement();
            // Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                // Close statement
                stmt.close();
            }
            // Close connection
            dbDisconnect();
        }
    }
}