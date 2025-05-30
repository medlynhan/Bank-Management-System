package solutions;
import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    private boolean verifyAccount(long account_number, String security_pin) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
        ps.setLong(1, account_number);
        ps.setString(2, security_pin);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private double getCurrentBalance(long account_number) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ?");
        ps.setLong(1, account_number);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("balance");
        }
        return -1;
    }

    private boolean updateBalance(long account_number, double amount, boolean isCredit) throws SQLException {
        String query = isCredit
            ? "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?"
            : "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, amount);
        ps.setLong(2, account_number);
        return ps.executeUpdate() > 0;
    }

    public void max(){
        scanner.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE balance = (SELECT MAX(balance) FROM Accounts)");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                System.out.println("Account_N0:\tName:\tBalance:");
                System.out.println("_______________________________________________________________");
                System.out.println(resultSet.getLong(1)+"||\t"+resultSet.getString(2)+"||\t"+resultSet.getDouble(4));
                System.out.println("_______________________________________________________________");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void min(){
        scanner.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE balance = (SELECT MIN(balance) FROM Accounts)");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                System.out.println("Account_N0:\tName:\tBalance:");
                System.out.println("_______________________________________________________________");
                System.out.println(resultSet.getLong(1)+"||\t"+resultSet.getString(2)+"||\t"+resultSet.getDouble(4));
                System.out.println("_______________________________________________________________");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_number != 0 && verifyAccount(account_number, security_pin)) {
                if (updateBalance(account_number, amount, true)) {
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
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_number != 0 && verifyAccount(account_number, security_pin)) {
                double current_balance = getCurrentBalance(account_number);
                if (amount <= current_balance) {
                    if (updateBalance(account_number, amount, false)) {
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
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0 && verifyAccount(sender_account_number, security_pin)) {
                double current_balance = getCurrentBalance(sender_account_number);
                if (amount <= current_balance) {
                    if (updateBalance(sender_account_number, amount, false) && updateBalance(receiver_account_number, amount, true)) {
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

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            if (verifyAccount(account_number, security_pin)) {
                double balance = getCurrentBalance(account_number);
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}