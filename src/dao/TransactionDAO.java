package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Transaction;
import db.DbCon;

public class TransactionDAO {

    public void addTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, txn_type, amount, txn_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbCon.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, t.getAccount_Id());
            ps.setString(2, t.getTxnType());
            ps.setDouble(3, t.getAcc_bal());
            ps.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    public List<Transaction> getTransactions(long accountId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT txn_id, txn_type, amount, txn_time FROM transactions WHERE account_id=? ORDER BY txn_id DESC";
        try (Connection conn = DbCon.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setTxnid(rs.getLong("txn_id"));
                    t.setTxnType(rs.getString("txn_type"));
                    t.setAcc_bal(rs.getDouble("amount"));
                    Timestamp ts = rs.getTimestamp("txn_time");
                    if (ts != null)
                        t.setTimeStamp(ts.toLocalDateTime());
                    list.add(t);
                }
            }
        }
        return list;
    }

    public String getTransactionSummary(long accountId) throws SQLException {
        String sql = """
            SELECT 
                SUM(CASE WHEN txn_type='DEPOSIT' THEN amount ELSE 0 END) AS total_deposits,
                SUM(CASE WHEN txn_type='WITHDRAW' THEN amount ELSE 0 END) AS total_withdrawals
            FROM transactions
            WHERE account_id=?
            """;
        try (Connection conn = DbCon.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double deposits = rs.getDouble("total_deposits");
                    double withdrawals = rs.getDouble("total_withdrawals");
                    double net = deposits - withdrawals;
                    return "Total Deposits: ₹" + deposits +
                           "\nTotal Withdrawals: ₹" + withdrawals +
                           "\nNet Change: ₹" + net;
                }
            }
        }
        return "No transactions found for this account.";
    }
}
