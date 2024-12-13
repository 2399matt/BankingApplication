package org.example;

import java.sql.SQLException;
import java.util.Scanner;

import org.example.common.*;
import org.example.UI.UserInterface;

public class Main {
    public static void main(String[] args) {
        try {
            Bank bank = new Bank();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        UserInterface UI = new UserInterface();
        Scanner scanner = new Scanner(System.in);
        UI.start(scanner);
    }
}