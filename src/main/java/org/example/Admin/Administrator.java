package org.example.Admin;

import org.example.common.Person;
import org.example.DB.DBCon;
import java.util.LinkedList;
import java.util.List;

import java.sql.*;

public class Administrator extends Person {
    private final DBCon DB = new DBCon();
    private final Connection con;

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
    public String getUsersToRemove(){
        List<Person> list = new LinkedList<>();
        {
            try{
                PreparedStatement st = con.prepareStatement("SELECT * FROM \"Work\" WHERE remove=?");
                st.setInt(1,1);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    String username = rs.getString("username");
                    String pass = rs.getString("password");
                    String email = rs.getString("email");
                    Person person = new Person(username,pass,email);
                    list.add(person);
                }
            }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
        return list.toString();
    }
    public void fulfillDeletionRequests(){
        {
            try{
                PreparedStatement st = con.prepareStatement("DELETE FROM \"Work\" WHERE remove=?");
                st.setInt(1,1);
                st.executeUpdate();
                st.close();
                System.out.println("Accounts removed.");
            }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}
