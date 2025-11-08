package model;
import java.time.LocalDateTime;
public class Transaction {
    private long txnid;
    private long account_Id;
    private String txnType;
    private LocalDateTime timeStamp;
    private double acc_bal;

    public Transaction()
    {
        
    }
    public Transaction(long account_Id,String txnType,double acc_bal)
    {
        this.acc_bal=acc_bal;
        this.txnType=txnType;
        this.account_Id=account_Id;
    }

    public long getTxnid() {
        return txnid;
    }

    public void setTxnid(long txnid) {
        this.txnid = txnid;
    }

    public long getAccount_Id() {
        return account_Id;
    }

    public void setAccount_Id(long account_Id) {
        this.account_Id = account_Id;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAcc_bal() {
        return acc_bal;
    }

    public void setAcc_bal(double acc_bal) {
        this.acc_bal = acc_bal;
    }


}
