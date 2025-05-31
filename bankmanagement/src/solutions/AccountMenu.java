package solutions;

import java.sql.SQLException;
import java.util.Scanner;

public class AccountMenu {
    private final Scanner scanner;
    private final AccountManager accountManager;
    private final long accountNumber;

    public AccountMenu(Scanner scanner, AccountManager accountManager, long accountNumber) {
        this.scanner = scanner;
        this.accountManager = accountManager;
        this.accountNumber = accountNumber;
    }

    public void show() {
        int choice = 0;
        while (choice != 5) {
            printAccountMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    try {
                        accountManager.debit_money(accountNumber);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        accountManager.credit_money(accountNumber);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        accountManager.transfer_money(accountNumber);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    accountManager.getTotalAccountBalance(accountNumber);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Enter Valid Choice!");
                    break;
            }
        }
    }

    private void printAccountMenu() {
        System.out.println();
        System.out.println("1. Debit Money");
        System.out.println("2. Credit Money");
        System.out.println("3. Transfer Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Log Out");
        System.out.print("Enter your choice: ");
    }
} 
