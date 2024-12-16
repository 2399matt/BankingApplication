package org.example.Admin;

import org.example.common.Person;
import org.example.DB.DBCon;

import java.sql.*;

public class Administrator extends Person {
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
        super("", "admin", "admin@root.com");
    }

    public boolean authAdmin(String email, String pass) {
        return (email.equals(this.getEmail()) && pass.equals(this.getPass()));
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

    public void removeUser(String email, String password) {
        {
            try {
                PreparedStatement st = con.prepareStatement("DELETE FROM \"Work\" WHERE email=? AND password=?");
                st.setString(1, email);
                st.setString(2, password);
                st.executeUpdate();
                st.close();
                System.out.println("Account successfully removed!");
            } catch (SQLException e) {
                System.out.println("Unable to locate account.");
            }
        }
    }
}
