package dao;

import java.sql.*;
import java.util.*;
import db.DbCon;
import model.Customer;
import model.Account;

public class CustomerDAO {

    public long createCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"customer_id"})) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getMobile());
            ps.setString(4, c.getAddress());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getLong(1);
            }
        }
        return -1;
    }

    public List<Customer> getCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id, name, email, phone FROM customers ORDER BY customer_id";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomer_Id(rs.getLong("customer_id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setMobile(rs.getString("phone"));
                list.add(c);
            }
        }
        return list;
    }

    public Customer findCustomerById(long id) throws SQLException {
        String sql = "SELECT customer_id, name, email, phone, address FROM customers WHERE customer_id=?";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setCustomer_Id(rs.getLong("customer_id"));
                    c.setName(rs.getString("name"));
                    c.setEmail(rs.getString("email"));
                    c.setMobile(rs.getString("phone"));
                    c.setAddress(rs.getString("address"));
                    return c;
                }
            }
        }
        throw new SQLException("Customer not found: " + id);
    }

    public List<Customer> findCustomerByName(String name) throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id, name, email, phone, address FROM customers WHERE LOWER(name) LIKE ?";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setCustomer_Id(rs.getLong("customer_id"));
                    c.setName(rs.getString("name"));
                    c.setEmail(rs.getString("email"));
                    c.setMobile(rs.getString("phone"));
                    c.setAddress(rs.getString("address"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    public void updateCustomer(Customer c) throws SQLException {
        String sql = "UPDATE customers SET email=?, phone=?, address=? WHERE customer_id=?";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getEmail());
            ps.setString(2, c.getMobile());
            ps.setString(3, c.getAddress());
            ps.setLong(4, c.getCustomer_Id());
            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SQLException("Customer not found: " + c.getCustomer_Id());
        }
    }

    public List<Account> getAccountsByCustomer(long customerId) throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT account_id, account_type, balance, status FROM accounts WHERE customer_id=?";
        try (Connection con = DbCon.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setAccountId(rs.getLong("account_id"));
                    a.setType(rs.getString("account_type"));
                    a.setBalance(rs.getDouble("balance"));
                    list.add(a);
                }
            }
        }
        return list;
    }
}
