package org.example.common;

import org.example.DB.DBCon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Person {
    private String username;
    private String password;
    private String email;
    private Bank bank;
    private DBCon DB = new DBCon();
    private Connection con;

    {
        try {
            con = DB.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person() {
    }

    public Person(String newUser, String newPass, String newEmail) {
        this.username = newUser;
        this.password = newPass;
        this.email = newEmail;

    }

    public void addPerson(Person person, double balance) throws SQLException {
        String sql = "SELECT * FROM \"Work\" WHERE username=?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, person.username);
        ResultSet rs = st.executeQuery();
        if (!rs.next()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"Work\"(\"ID\",username,password,balance,email)VALUES(DEFAULT,?,?,DEFAULT,?)");
            ps.setString(1, person.username);
            ps.setString(2, person.password);
            ps.setString(3,person.email);
            ps.executeUpdate();
            bank = new Bank();
            bank.deposit(person, balance);
        } else {
            System.out.println("Account already exists.");
            return;
        }

    }


    public String getUser() {
        return this.username;
    }

    public Person getPersonFromEmail(String email, String password){
        {
            try{
                PreparedStatement st = con.prepareStatement("SELECT * FROM \"Work\" WHERE email=?");
                st.setString(1,email);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    String user = rs.getString("username");
                    String pass = rs.getString("password");
                    if(pass.equals(password))
                        return new Person(user,pass,email);
                    else
                        return new Person();
                }else{
                    return new Person();
                }
            }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }

    }

    public String getPass() {
        return this.password;
    }

    public String getEmail(){return this.email;}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }
}
