package model;

public class Account {
    private long accountId;
    private long customerId;
    private String type;
    private double balance;

    public Account() {
    }

    public Account(long accountId, long customerId, String type, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.type = type;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
