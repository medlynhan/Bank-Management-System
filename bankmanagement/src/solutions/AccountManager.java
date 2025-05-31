package solutions;
import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    private AccountRepository accRepo;

    AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
        this.accRepo = new AccountRepository(connection);
    }
    
    public void getMaxAccountBalance(){
        scanner.nextLine();
        try {
            ResultSet resultSet = accRepo.getMaxBalance();
            if(resultSet.next()) {
                printAccountDetails(resultSet);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void getMinAccountBalance(){
        scanner.nextLine();
        try {
            ResultSet resultSet = accRepo.getMinBalance();
            if(resultSet.next()) {
                printAccountDetails(resultSet);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        double amount = inputAmount();
        SecurityPin security_pin = inputPin();
        
        try {
            connection.setAutoCommit(false);
            if (account_number != 0 && accRepo.verifyAccount(account_number, security_pin.getPin())) {
                if (accRepo.updateBalance(account_number, amount, true)) {
                    System.out.println("Rs." + amount + " credited Successfully");
                    connection.commit();
                } else {
                    System.out.println("Transaction Failed!");
                    connection.rollback();
                }
            } else {
                System.out.println("Invalid Security Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        double amount = inputAmount();
        SecurityPin security_pin = inputPin();
        
        try {
            connection.setAutoCommit(false);
            if (account_number != 0 && accRepo.verifyAccount(account_number, security_pin.getPin())) {
                double current_balance = accRepo.getCurrentBalance(account_number);
                if (amount <= current_balance) {
                    if (accRepo.updateBalance(account_number, amount, false)) {
                        System.out.println("Rs." + amount + " debited Successfully");
                        connection.commit();
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        double amount = inputAmount();
        SecurityPin security_pin = inputPin();
        
        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0 && accRepo.verifyAccount(sender_account_number, security_pin.getPin())) {
                double current_balance = accRepo.getCurrentBalance(sender_account_number);
                if (amount <= current_balance) {
                    if (accRepo.updateBalance(sender_account_number, amount, false) && accRepo.updateBalance(receiver_account_number, amount, true)) {
                        System.out.println("Transaction Successful!");
                        System.out.println("Rs." + amount + " Transferred Successfully");
                        connection.commit();
                    } else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            } else {
                System.out.println("Invalid account number or Security Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    public void getTotalAccountBalance(long account_number){
        SecurityPin security_pin = inputPin();
        try {
            if (accRepo.verifyAccount(account_number, security_pin.getPin())) {
                double balance = accRepo.getCurrentBalance(account_number);
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printAccountDetails(ResultSet resultSet) throws SQLException {
        System.out.println("Account_N0:\tName:\tBalance:");
        System.out.println("_______________________________________________________________");
        System.out.println(resultSet.getLong(1)+"||\t"+resultSet.getString(2)+"||\t"+resultSet.getDouble(4));
        System.out.println("_______________________________________________________________");
    }

    private double inputAmount() {
        System.out.print("Enter Amount: ");
        return scanner.nextDouble();
    }

    private SecurityPin inputPin() {
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String pin = scanner.nextLine();
        return new SecurityPin(pin);
    }
}