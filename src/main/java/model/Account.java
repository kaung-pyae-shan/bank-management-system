package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

	private int id;
	private AccountType accountType;
	private String accountNumber;
	private BigDecimal balance;
	private AccountStatus status;
	private LocalDateTime createdAt;
	private Staff createdBy;
	private AccountType.OwnershipType ownershipType;
	private int staffId;

	public AccountType.OwnershipType getOwnershipType() {
	    return ownershipType;
	}

	public void setOwnershipType(AccountType.OwnershipType ownershipType) {
	    this.ownershipType = ownershipType;
	}

	
	public enum AccountStatus {
		ACTIVE, INACTIVE, CLOSED
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Staff getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Staff createdBy) {
		this.createdBy = createdBy;
	}
	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
}
