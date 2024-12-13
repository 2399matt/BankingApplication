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


    public void deposit(Person person, double num) {
        double bal = 0;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM \"Work\" WHERE email=?");
            ps.setString(1, person.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bal = rs.getInt("balance");
                bal += num;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE email=?");
            ps.setDouble(1, bal);
            ps.setString(2, person.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public double getBalance(Person person) {

        {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT balance FROM \"Work\" WHERE email=?");
                ps.setString(1, person.getEmail());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("balance");
                } else {
                    return 0;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void withdrawal(Person person, double num) {
        double bal = 0;
        {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT balance FROM \"Work\" WHERE email=?");
                ps.setString(1, person.getEmail());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    bal = rs.getDouble("balance");
                } else {
                    System.out.println("No user found!");
                    return;
                }
                if (bal < num) {
                    System.out.println("Not enough funds!");
                } else {
                    bal -= num;
                    ps = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE email=?");
                    ps.setDouble(1, bal);
                    ps.setString(2, person.getEmail());
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
                PreparedStatement st = con.prepareStatement("SELECT * FROM \"Work\" WHERE email=?");
                st.setString(1, person.getEmail());
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

    public void transfer(Person from, Person to, double balance) {
        {
            try {
                PreparedStatement st = con.prepareStatement("SELECT balance FROM \"Work\" WHERE email=?");
                st.setString(1, from.getEmail());
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    double sourceBal = rs.getDouble("balance");
                    if (sourceBal > balance) {
                        sourceBal -= balance;
                        st = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE email=?");
                        st.setDouble(1, sourceBal);
                        st.setString(2, from.getEmail());
                    } else {
                        System.out.println("Not enough funds.");
                        return;
                    }
                    PreparedStatement stTwo = con.prepareStatement("SELECT balance FROM \"Work\" WHERE email=?");
                    stTwo.setString(1, to.getEmail());
                    rs = stTwo.executeQuery();
                    if (rs.next()) {
                        double toBalance = rs.getDouble("balance");
                        toBalance += balance;
                        stTwo = con.prepareStatement("UPDATE \"Work\" SET balance=? WHERE email=?");
                        stTwo.setDouble(1, toBalance);
                        stTwo.setString(2, to.getEmail());
                        stTwo.executeUpdate();
                        st.executeUpdate();
                        st.close();
                        stTwo.close();
                        rs.close();
                        System.out.println("Transfer to: " + to.getEmail() + " successful!");
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
