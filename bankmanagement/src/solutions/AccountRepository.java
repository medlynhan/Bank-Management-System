package solutions;
import java.sql.*;

//repository ini bakal ngehandle semua operasi database yang berhubungan dengan akun

public class AccountRepository {
    private Connection connection;

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    protected boolean verifyAccount(long account_number, String security_pin) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
        ps.setLong(1, account_number);
        ps.setString(2, security_pin);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    protected double getCurrentBalance(long account_number) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ?");
        ps.setLong(1, account_number);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("balance");
        }
        return -1;
    }

    protected boolean updateBalance(long account_number, double amount, boolean isCredit) throws SQLException {
        String query = isCredit
            ? "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?"
            : "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, amount);
        ps.setLong(2, account_number);
        return ps.executeUpdate() > 0;
    }

    public ResultSet getMinBalance() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Accounts WHERE balance = (SELECT MIN(balance) FROM Accounts)");
        return ps.executeQuery();
    }

    public ResultSet getMaxBalance() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Accounts WHERE balance = (SELECT MAX(balance) FROM Accounts)");
        return ps.executeQuery();
    }

    public long makeNewAccount(long account_number, String full_name, String email, double balance, String security_pin) throws SQLException {
    	String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
        preparedStatement.setLong(1, account_number);
        preparedStatement.setString(2, full_name);
        preparedStatement.setString(3, email);
        preparedStatement.setDouble(4, balance);
        preparedStatement.setString(5, security_pin);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            return account_number;
        } else {
            throw new RuntimeException("Account Creation failed!!");
        }
        
    }

    public ResultSet getAccountNumberByEmail(String email) throws SQLException {
    	String query = "SELECT account_number from Accounts WHERE email = ?";
    	PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        return preparedStatement.executeQuery();
    }

    public ResultSet getTheLastAccountNumber() throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT top 1 account_number from Accounts ORDER BY account_number DESC");
    }
}
