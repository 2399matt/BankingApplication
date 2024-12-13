package org.example.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
    private final String user = "postgres";
    private final String pass = "Seanmalto2399";
    private final String url = "jdbc:postgresql://localhost:5432/TestDB";

    public DBCon() {

    }

    public Connection createConnection() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }

    public void closeConnection() throws SQLException {
        createConnection().close();
    }


}
