package model.dto;

import java.math.BigDecimal;

public class AdminDashboardStats {

	private int customers;
	private int accounts;
	private BigDecimal bankBalance;
	private int pendingTransactions;

	public AdminDashboardStats(int customers, int accounts, BigDecimal bankBalance, int pendingTransactions) {
		super();
		this.customers = customers;
		this.accounts = accounts;
		this.bankBalance = bankBalance;
		this.pendingTransactions = pendingTransactions;
	}

	public int getCustomers() {
		return customers;
	}

	public int getAccounts() {
		return accounts;
	}

	public BigDecimal getBankBalance() {
		return bankBalance;
	}

	public int getPendingTransactions() {
		return pendingTransactions;
	}

}
