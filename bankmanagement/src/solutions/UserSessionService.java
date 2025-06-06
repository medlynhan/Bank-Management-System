package solutions;

import java.sql.SQLException;
import java.util.Scanner;

public class UserSessionService {
    private final Scanner scanner;
    private final Accounts accounts;
    private final AccountManager accountManager;
    private final UserRepository userRepository;

    public UserSessionService(Scanner scanner, Accounts accounts, AccountManager accountManager, UserRepository userRepository) {
        this.scanner = scanner;
        this.accounts = accounts;
        this.accountManager = accountManager;
        this.userRepository = userRepository;
    }

    public void register() {
        scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if(userRepository.userExists(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        if (userRepository.registerUser(fullName, email, password)) {
            System.out.println("Registration Successful!");
        } else {
            System.out.println("Registration Failed!");
        }
    }

    public void handleLogin() {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userRepository.validateLogin(email, password)) {
            System.out.println("\nUser Logged In!");
            long accountNumber = checkAccountExist(email);
            if (accountNumber == 0) {
                System.out.println("There is no account.");
                return;
            }
            loginMenu(accountNumber);
        } else {
            System.out.println("Invalid Email or Password!");
        }
    }

    private long checkAccountExist(String email) {
        if (!accounts.account_exist(email)) {
            System.out.println("\n1. Open a new Bank Account");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int subChoice = scanner.nextInt();
            if (subChoice == 1) {
                long accountNumber = accounts.open_account(email);
                System.out.println("Account Created Successfully");
                System.out.println("Your Account Number is: " + accountNumber);
                return accountNumber;
            } else {
                return 0;
            }
        }
        return accounts.getAccount_number(email);
    }

    private void loginMenu(long accountNumber) {
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
        System.out.println("\nAccount Menu:");
        System.out.println("1. Debit Money");
        System.out.println("2. Credit Money");
        System.out.println("3. Transfer Money");
        System.out.println("4. Check Total Account Balance");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }
}
