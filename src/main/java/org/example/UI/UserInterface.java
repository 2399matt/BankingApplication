package org.example.UI;

import org.example.Admin.Administrator;
import org.example.common.*;

import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {
    private Person person = new Person();
    private Bank bank;
    private Administrator admin;

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

            System.out.println("[1] Create account\n[2] Login\n[3] Admin Login\n[4] Exit");
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 1) {
                System.out.println("Desired username: ");
                String user = scanner.nextLine();
                System.out.println("Desired password: ");
                String pass = scanner.nextLine();
                System.out.println("Enter email address: ");
                String email = scanner.nextLine();
                while (!email.contains("@")) {
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
                while (!email.contains("@")) {
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
                admin = new Administrator();
                System.out.println("Enter email: ");
                String email = scanner.nextLine();
                System.out.println("Enter password: ");
                String pass = scanner.nextLine();
                if (admin.authAdmin(email, pass)) {
                    adminDetails(scanner);
                } else {
                    System.out.println("Invalid login.");
                }

            } else if (input == 4) {
                System.exit(0);
            }
        }

    }

    public void bankDetails(Scanner scanner, Person person) {
        while (true) {
            System.out.println("[1] Deposit\n[2] Withdrawal\n[3] Check balance\n[4] Transfer\n[5] Update Email\n[6] Logout");
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
                Person transferee = new Person("", "", transferEmail);
                bank.transfer(person, transferee, transferAmount);
            } else if (input == 5) {
                AccUpdate update = new AccUpdate();
                System.out.println("Confirm current email address:");
                String currEmail = scanner.nextLine();
                while(!currEmail.equals(person.getEmail())){
                    System.out.println("Email does not match email on file. Please try again: ");
                    currEmail = scanner.nextLine();
                }
                System.out.println("Enter new email address:");
                String newEmail = scanner.nextLine();
                while(!newEmail.contains("@")){
                    System.out.println("Enter new email address:");
                    newEmail = scanner.nextLine();
                }
                update.updateEmail(person, newEmail);
            } else if (input == 6) {
                start(scanner);
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    public void adminDetails(Scanner scanner) {
        while (true) {
            System.out.println("[1] Check current bank holdings\n[2] Logout");
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 1) {
                admin.getBankBalance();
            } else if (input == 2) {
                start(scanner);
            }
        }
    }


}
