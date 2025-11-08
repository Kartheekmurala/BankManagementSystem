package gui;

import dao.TransactionDAO;
import model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionPanel extends JPanel {

    private final TransactionDAO dao = new TransactionDAO();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    public TransactionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JTextField accIdField = new JTextField(15);
        JButton viewBtn = new JButton("View Transactions");
        JButton summaryBtn = new JButton("Transaction Summary");

        topPanel.add(new JLabel("Account ID:"));
        topPanel.add(accIdField);
        topPanel.add(viewBtn);
        topPanel.add(summaryBtn);

        JTextArea area = new JTextArea(18, 70);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(accIdField.getText().trim());
                List<Transaction> list = dao.getTransactions(id);
                area.setText("");
                if (list.isEmpty()) {
                    area.setText("No transactions found for Account ID: " + id);
                } else {
                    area.append(String.format("%-10s %-12s %-12s %-22s%n", "Txn ID", "Type", "Amount", "Date & Time"));
                    area.append("-------------------------------------------------------------\n");
                    for (Transaction t : list) {
                        String time = t.getTimeStamp() != null ? t.getTimeStamp().format(fmt) : "N/A";
                        area.append(String.format("%-10d %-12s %-12.2f %-22s%n",
                                t.getTxnid(), t.getTxnType(), t.getAcc_bal(), time));
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        summaryBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(accIdField.getText().trim());
                String s = dao.getTransactionSummary(id);
                JOptionPane.showMessageDialog(this, s, "Transaction Summary", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
