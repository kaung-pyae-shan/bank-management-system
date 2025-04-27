package model.dto;

import java.math.BigDecimal;

public class AdminDashboardStats {

	private long customers;
	private long accounts;
	private BigDecimal bankBalance;
	private long transactionTodays;

	public AdminDashboardStats(long customers, long accounts, BigDecimal bankBalance, long transactionTodays) {
		super();
		this.customers = customers;
		this.accounts = accounts;
		this.bankBalance = bankBalance;
		this.transactionTodays = transactionTodays;
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

	public long getTransactionTodays() {
		return transactionTodays;
	}

}
