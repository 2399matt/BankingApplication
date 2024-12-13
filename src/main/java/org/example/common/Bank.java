package org.example.common;

import org.example.DB.DBCon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bank {
    private final DBCon DB = new DBCon();
    private final Connection con;

    {
        try {
            con = DB.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Bank() throws SQLException {
    }


    public void deposit(Person person, int num) {
        int bal = 0;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM \"Work\" WHERE username=?");
            ps.setString(1, person.getUser());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bal = rs.getInt("balance");
                bal += num;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE username=?");
            ps.setInt(1, bal);
            ps.setString(2, person.getUser());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public int getBalance(Person person) {

        {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT balance FROM \"Work\" WHERE username=?");
                ps.setString(1, person.getUser());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void withdrawal(Person person, int num) {
        int bal = 0;
        {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT balance FROM \"Work\" WHERE username=?");
                ps.setString(1, person.getUser());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    bal = rs.getInt("balance");
                } else {
                    System.out.println("No user found!");
                    return;
                }
                if (bal < num) {
                    System.out.println("Not enough funds!");
                } else {
                    bal -= num;
                    ps = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE username=?");
                    ps.setInt(1, bal);
                    ps.setString(2, person.getUser());
                    ps.executeUpdate();
                    System.out.println("Withdrawl successful. New balance: $" + bal);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public boolean authUser(Person person) {
        {
            try {
                PreparedStatement st = con.prepareStatement("SELECT * FROM \"Work\" WHERE username=?");
                st.setString(1, person.getUser());
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    if (rs.getString("password").equals(person.getPass())) {
                        System.out.println("Welcome back, " + person.getUser() + "!");
                        return true;
                    }
                } else {
                    return false;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public void transfer(Person from, Person to, int balance) {
        {
            try {
                PreparedStatement st = con.prepareStatement("SELECT balance FROM \"Work\" WHERE username=?");
                st.setString(1, from.getUser());
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    int sourceBal = rs.getInt("balance");
                    if (sourceBal > balance) {
                        sourceBal -= balance;
                        st = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE username=?");
                        st.setInt(1, sourceBal);
                        st.setString(2, from.getUser());
                    } else {
                        System.out.println("Not enough funds.");
                        return;
                    }
                    PreparedStatement stTwo = con.prepareStatement("SELECT balance FROM \"Work\" WHERE username=?");
                    stTwo.setString(1, to.getUser());
                    rs = stTwo.executeQuery();
                    if (rs.next()) {
                        int toBalance = rs.getInt("balance");
                        toBalance += balance;
                        stTwo = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE username=?");
                        stTwo.setInt(1, toBalance);
                        stTwo.setString(2, to.getUser());
                        stTwo.executeUpdate();
                        st.executeUpdate();
                        st.close();
                        stTwo.close();
                        rs.close();
                        System.out.println("Transfer to: " + to.getUser() + " successful!");
                    } else {
                        System.out.println("Unable to locate recipient.");
                        return;
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
