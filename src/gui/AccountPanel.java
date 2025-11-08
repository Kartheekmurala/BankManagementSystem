package gui;

import dao.AccountDAO;
import javax.swing.*;
import java.awt.*;

public class AccountPanel extends JPanel {

    private final AccountDAO dao = new AccountDAO();

    public AccountPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField custIdField = new JTextField(15);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"SAVINGS", "CURRENT"});
        JTextField initField = new JTextField("", 15);
        JButton createBtn = new JButton("Create Account");

        JTextField accIdField = new JTextField(15);
        JTextField amtField = new JTextField("", 15);
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton balanceBtn = new JButton("Check Balance");
        JButton detailsBtn = new JButton("View Details");
        JButton transferBtn = new JButton("Transfer Funds");
        JButton closeBtn = new JButton("Close Account");

        Dimension btnSize = new Dimension(160, 30);
        for (JButton b : new JButton[]{createBtn, depositBtn, withdrawBtn, balanceBtn, detailsBtn, transferBtn, closeBtn}) {
            b.setPreferredSize(btnSize);
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1; formPanel.add(custIdField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1; formPanel.add(typeBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Initial Balance:"), gbc);
        gbc.gridx = 1; formPanel.add(initField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(createBtn, gbc);
        gbc.gridwidth = 1;

        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Account ID:"), gbc);
        gbc.gridx = 1; formPanel.add(accIdField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; formPanel.add(amtField, gbc);

        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        midPanel.add(depositBtn);
        midPanel.add(withdrawBtn);
        midPanel.add(balanceBtn);
        midPanel.add(detailsBtn);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.add(transferBtn);
        bottomPanel.add(closeBtn);

        
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(midPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(bottomPanel);

        add(centerPanel, BorderLayout.CENTER);

        createBtn.addActionListener(e -> {
            try {
                long id = dao.createAccount(
                        Long.parseLong(custIdField.getText().trim()),
                        (String) typeBox.getSelectedItem(),
                        Double.parseDouble(initField.getText().trim())
                );
                JOptionPane.showMessageDialog(this, "Account created successfully. ID: " + id);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        depositBtn.addActionListener(e -> {
            try {
                dao.deposit(Long.parseLong(accIdField.getText().trim()),
                            Double.parseDouble(amtField.getText().trim()));
                JOptionPane.showMessageDialog(this, "Deposit successful.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        withdrawBtn.addActionListener(e -> {
            try {
                dao.withdraw(Long.parseLong(accIdField.getText().trim()),
                             Double.parseDouble(amtField.getText().trim()));
                JOptionPane.showMessageDialog(this, "Withdrawal successful.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        balanceBtn.addActionListener(e -> {
            try {
                double bal = dao.getBalance(Long.parseLong(accIdField.getText().trim()));
                JOptionPane.showMessageDialog(this, "Current Balance: " + bal);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        detailsBtn.addActionListener(e -> {
            try {
                var acc = dao.getAccountDetails(Long.parseLong(accIdField.getText().trim()));
                JOptionPane.showMessageDialog(this,
                        "Account ID: " + acc.getAccountId() + "\n" +
                        "Customer ID: " + acc.getCustomerId() + "\n" +
                        "Type: " + acc.getType() + "\n" +
                        "Balance: " + acc.getBalance());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        transferBtn.addActionListener(e -> {
            try {
                String from = JOptionPane.showInputDialog("Enter Source Account ID:");
                String to = JOptionPane.showInputDialog("Enter Destination Account ID:");
                String amt = JOptionPane.showInputDialog("Enter Transfer Amount:");
                dao.transferFunds(Long.parseLong(from.trim()), Long.parseLong(to.trim()), Double.parseDouble(amt.trim()));
                JOptionPane.showMessageDialog(this, "Transfer successful.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        closeBtn.addActionListener(e -> {
            try {
                dao.closeAccount(Long.parseLong(accIdField.getText().trim()));
                JOptionPane.showMessageDialog(this, "Account closed successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
