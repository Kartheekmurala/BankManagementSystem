package gui;

import javax.swing.*;
import dao.CustomerDAO;
import model.Customer;
import model.Account;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {

    private final CustomerDAO dao = new CustomerDAO();

    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField addrField = new JTextField(15);

        JButton addBtn = new JButton("Add Customer");
        JButton updateBtn = new JButton("Update Customer");
        JButton searchIdBtn = new JButton("Search by ID");
        JButton searchNameBtn = new JButton("Search by Name");
        JButton viewAccBtn = new JButton("View Accounts");

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Customer ID (for search/update):"), gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; formPanel.add(emailField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; formPanel.add(phoneField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; formPanel.add(addrField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(searchIdBtn);
        buttonPanel.add(searchNameBtn);
        buttonPanel.add(viewAccBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            try {
                Customer c = new Customer(nameField.getText(), emailField.getText(), phoneField.getText(), addrField.getText());
                long id = dao.createCustomer(c);
                JOptionPane.showMessageDialog(this, "Customer added. ID: " + id);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        updateBtn.addActionListener(e -> {
            try {
                Customer c = new Customer();
                c.setCustomer_Id(Long.parseLong(idField.getText()));
                c.setEmail(emailField.getText());
                c.setMobile(phoneField.getText());
                c.setAddress(addrField.getText());
                dao.updateCustomer(c);
                JOptionPane.showMessageDialog(this, "Customer updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        searchIdBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(idField.getText());
                Customer c = dao.findCustomerById(id);
                nameField.setText(c.getName());
                emailField.setText(c.getEmail());
                phoneField.setText(c.getMobile());
                addrField.setText(c.getAddress());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        searchNameBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                List<Customer> list = dao.findCustomerByName(name);
                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No customer found with name: " + name);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Customer c : list) {
                        sb.append("ID: ").append(c.getCustomer_Id())
                          .append(" | Name: ").append(c.getName())
                          .append(" | Email: ").append(c.getEmail())
                          .append(" | Phone: ").append(c.getMobile()).append("\n");
                    }
                    JTextArea area = new JTextArea(sb.toString());
                    area.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(area), "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        viewAccBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(idField.getText());
                List<Account> list = dao.getAccountsByCustomer(id);
                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No accounts found for customer ID: " + id);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Account a : list) {
                        sb.append("Account ID: ").append(a.getAccountId())
                          .append(" | Type: ").append(a.getType())
                          .append(" | Balance: ").append(a.getBalance()).append("\n");
                    }
                    JTextArea area = new JTextArea(sb.toString());
                    area.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(area), "Customer Accounts", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
