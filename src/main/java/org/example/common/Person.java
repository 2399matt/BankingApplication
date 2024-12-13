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

    public Person(String newUser, String newPass) {
        this.username = newUser;
        this.password = newPass;

    }

    public void addPerson(Person person, int balance) throws SQLException {
        String sql = "SELECT * FROM \"Work\" WHERE username=?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, person.username);
        ResultSet rs = st.executeQuery();
        if (!rs.next()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"Work\"(\"ID\",username,password,balance)VALUES(DEFAULT,?,?,DEFAULT)");
            ps.setString(1, person.username);
            ps.setString(2, person.password);
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

    public String getPass() {
        return this.password;
    }

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
