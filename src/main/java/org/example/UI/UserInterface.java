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
                System.out.println("Enter email address: ");
                String email = scanner.nextLine();
                while(!email.contains("@")){
                    System.out.println("Enter a valid email address: ");
                     email = scanner.nextLine();
                }
                System.out.println("Starting balance: ");
                double balance = Double.parseDouble(scanner.nextLine());
                person = new Person(user, pass, email);
                try {
                    person.addPerson(person, balance);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else if (input == 2) {
                System.out.println("Email: ");
                String email = scanner.nextLine();
                while(!email.contains("@")){
                    System.out.println("Email: ");
                    email = scanner.nextLine();
                }
                System.out.println("Password: ");
                String pass = scanner.nextLine();
                person = person.getPersonFromEmail(email, pass);
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
                bank.deposit(this.person, Double.parseDouble(scanner.nextLine()));

            } else if (input == 2) {
                System.out.println("How much to withdrawal? ");
                double num = Double.parseDouble(scanner.nextLine());
                bank.withdrawal(this.person, num);

            } else if (input == 3) {
                System.out.println("Current balance: $" + bank.getBalance(this.person));
            } else if (input == 4) {
                System.out.println("Email of person to transfer to?");
                String transferEmail = scanner.nextLine();
                System.out.println("Amount to transfer?");
                double transferAmount = Double.parseDouble(scanner.nextLine());
                Person transferee = new Person("", "",transferEmail);
                bank.transfer(person, transferee, transferAmount);
            } else if (input == 5) {
                start(scanner);
            } else {
                System.out.println("Invalid command.");
            }
        }
    }


}
