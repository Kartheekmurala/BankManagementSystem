package gui;
import javax.swing.*;


public class MainFrame extends JFrame
{
    public MainFrame()
    {
        setTitle("Bank Management System");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Customers", new CustomerPanel());
        tabs.add("Accounts", new AccountPanel());
        tabs.add("Transactions", new TransactionPanel());

        add(tabs);
        setLocationRelativeTo(null);
    }
    
}
