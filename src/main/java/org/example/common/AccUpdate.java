package org.example.common;

import org.example.common.Person;
import org.example.DB.DBCon;

import java.sql.*;

public class AccUpdate {
    private DBCon DB = new DBCon();
    private Connection con;

    {
        try {
            con = DB.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AccUpdate() {
    }

    public void updateEmail(Person person, String email) {
        {
            try {
                PreparedStatement st = con.prepareStatement("UPDATE \"Work\" SET email=? WHERE email=?");
                st.setString(2, person.getEmail());
                st.setString(1, email);
                st.executeUpdate();
                person.setEmail(email);
                st.close();
                System.out.println("Email updated successfully!");

            } catch (SQLException e) {
                System.out.println("Unable to locate provided email address.");
            }
        }
    }

    public void updatePassword(Person person, String password) {
        {
            try {
                PreparedStatement st = con.prepareStatement("UPDATE \"Work\" SET password=? WHERE email=?");
                st.setString(1, password);
                st.setString(2, person.getEmail());
                st.executeUpdate();
                st.close();
                System.out.println("Password successfully updated! ");
            } catch (SQLException e) {
                System.out.println("Unable to locate account.");
            }
        }
    }
    public void addUserForRemoval(Person person){
        {
            try{
                PreparedStatement st = con.prepareStatement("UPDATE \"Work\" SET remove=? WHERE email=? AND password=?");
                st.setInt(1,1);
                st.setString(2, person.getEmail());
                st.setString(3, person.getPass());
                st.executeUpdate();
                st.close();
                System.out.println("Request sent!");
            }catch(SQLException e){
                System.out.println("Unable to locate account.");
            }
        }
    }


}
