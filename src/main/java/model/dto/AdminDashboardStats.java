package model.dto;

import java.math.BigDecimal;

public class AdminDashboardStats {

	private long customers;
	private long accounts;
	private BigDecimal bankBalance;
	private long pendingTransactions;

	public AdminDashboardStats(long customers, long accounts, BigDecimal bankBalance, long pendingTransactions) {
		super();
		this.customers = customers;
		this.accounts = accounts;
		this.bankBalance = bankBalance;
		this.pendingTransactions = pendingTransactions;
	}

	public long getCustomers() {
		return customers;
	}

	public long getAccounts() {
		return accounts;
	}

	public BigDecimal getBankBalance() {
		return bankBalance;
	}

	public long getPendingTransactions() {
		return pendingTransactions;
	}

}
