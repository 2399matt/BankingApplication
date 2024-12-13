package org.example.UI;

import org.example.common.*;

import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {
    private Person person = new Person();
    private Bank bank;

    {
        try {
            bank = new Bank();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserInterface() {

    }

    public void start(Scanner scanner) {
        while (true) {

            System.out.println("[1] Create account\n[2] Login\n[3] Exit");
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 1) {
                System.out.println("Desired username: ");
                String user = scanner.nextLine();
                System.out.println("Desired password: ");
                String pass = scanner.nextLine();
                System.out.println("Starting balance: ");
                int balance = Integer.parseInt(scanner.nextLine());
                person = new Person(user, pass);
                try {
                    person.addPerson(person, balance);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else if (input == 2) {
                System.out.println("Username:");
                String user = scanner.nextLine();
                System.out.println("Password: ");
                String pass = scanner.nextLine();
                person = new Person(user, pass);
                if (bank.authUser(person)) {
                    bankDetails(scanner, person);
                } else {
                    System.out.println("Invalid login.");
                }
            } else if (input == 3) {

                System.exit(0);
            }
        }

    }

    public void bankDetails(Scanner scanner, Person person) {
        while (true) {
            System.out.println("[1] Deposit\n[2] Withdrawal\n[3] Check balance\n[4] Transfer\n[5] Logout");
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 1) {
                System.out.println("How much to deposit? ");
                bank.deposit(this.person, Integer.parseInt(scanner.nextLine()));
                //saveToFile("logins.txt");
            } else if (input == 2) {
                System.out.println("How much to withdrawal? ");
                int num = Integer.parseInt(scanner.nextLine());
                bank.withdrawal(this.person, num);
                // saveToFile("logins.txt");
            } else if (input == 3) {
                System.out.println("Current balance: $" + bank.getBalance(this.person));
            } else if (input == 4) {
                System.out.println("Name of person to transfer to?");
                String transferName = scanner.nextLine();
                System.out.println("Amount to transfer?");
                int transferAmount = Integer.parseInt(scanner.nextLine());
                Person transferee = new Person(transferName, "");
                bank.transfer(person, transferee, transferAmount);
            } else if (input == 5) {
                start(scanner);
            } else {
                System.out.println("Invalid command.");
            }
        }
    }


}
