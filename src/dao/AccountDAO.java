package dao;

import java.sql.*;
import db.DbCon;
import model.Account;

public class AccountDAO {

    public long createAccount(long customer_Id, String type, double initial) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, account_type, balance, status) VALUES (?, ?, ?, 'ACTIVE')";
        if (initial < 0)
            throw new SQLException("Initial balance cannot be negative!");

        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"account_id"})) {

            ps.setLong(1, customer_Id);
            ps.setString(2, type);
            ps.setDouble(3, initial);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
            }
        }
        return -1;
    }

    public void deposit(long account_Id, double amount) throws SQLException {
        if (amount <= 0)
            throw new SQLException("Deposit amount must be greater than 0!");

        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ? AND status = 'ACTIVE'";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setLong(2, account_Id);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SQLException("Account not found or closed: " + account_Id);
        }
    }

    public void withdraw(long account_Id, double amount) throws SQLException {
        if (amount <= 0)
            throw new SQLException("Withdrawal amount must be greater than 0!");

        String selectSql = "SELECT balance FROM accounts WHERE account_id = ? AND status = 'ACTIVE' FOR UPDATE";
        String updateSql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (Connection con = DbCon.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psSel = con.prepareStatement(selectSql)) {
                psSel.setLong(1, account_Id);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (!rs.next())
                        throw new SQLException("Account not found or closed: " + account_Id);

                    double balance = rs.getDouble("balance");
                    if (balance < amount)
                        throw new SQLException("Insufficient balance! Available: " + balance);

                    double newBal = balance - amount;
                    try (PreparedStatement psUpd = con.prepareStatement(updateSql)) {
                        psUpd.setDouble(1, newBal);
                        psUpd.setLong(2, account_Id);
                        psUpd.executeUpdate();
                    }

                    con.commit();
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public Account getAccountDetails(long account_Id) throws SQLException {
        String sql = "SELECT account_id, customer_id, account_type, balance, status FROM accounts WHERE account_id = ?";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, account_Id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setAccountId(rs.getLong("account_id"));
                    acc.setCustomerId(rs.getLong("customer_id"));
                    acc.setType(rs.getString("account_type"));
                    acc.setBalance(rs.getDouble("balance"));
                    return acc;
                }
            }
        }
        throw new SQLException("Account not found: " + account_Id);
    }

    public double getBalance(long account_Id) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ? AND status = 'ACTIVE'";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, account_Id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getDouble("balance");
            }
        }
        throw new SQLException("Account not found or closed: " + account_Id);
    }

    public void transferFunds(long fromAcc, long toAcc, double amount) throws SQLException {
        if (amount <= 0)
            throw new SQLException("Transfer amount must be greater than 0!");

        withdraw(fromAcc, amount);
        deposit(toAcc, amount);
    }

    public void closeAccount(long account_Id) throws SQLException {
        String sql = "UPDATE accounts SET status = 'CLOSED' WHERE account_id = ?";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, account_Id);
            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SQLException("Account not found: " + account_Id);
        }
    }
}
