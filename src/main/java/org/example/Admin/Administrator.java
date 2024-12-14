package org.example.Admin;

import org.example.common.Person;
import org.example.DB.DBCon;

import java.sql.*;

public class Administrator extends Person {
    private String email;
    private String password;
    private DBCon DB = new DBCon();
    private Connection con;

    {
        try {
            con = DB.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Administrator() {
        this.email = "admin@root.com";
        this.password = "admin";
    }

    public boolean authAdmin(String email, String pass) {
        return (email.equals(this.email) && pass.equals(this.password));
    }

    public void getBankBalance() {
        double balance = 0;
        {
            try {
                PreparedStatement st = con.prepareStatement("SELECT * FROM \"Work\"");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    balance += rs.getDouble("balance");
                }
                System.out.println("Current cash holdings: $" + balance);
                st.close();
                rs.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
