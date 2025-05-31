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
}
