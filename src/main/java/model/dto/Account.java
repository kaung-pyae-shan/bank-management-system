package model.dto;

public class Account {
    private String accountNumber;
    private String accountType;
    private String ownershipType;
    private double balance;
    private String status;

    // Constructor
    public Account(String accountNumber, String accountType, String ownershipType, double balance, String status) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ownershipType = ownershipType;
        this.balance = balance;
        this.status = status;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountType() { return accountType; }
    public String getOwnershipType() { return ownershipType; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
}
