package model.dto;

public class TellerDashboardStats {

	private long customers;
	private long accounts;
	private long cards;
	private long transactionTodays;

	public TellerDashboardStats(long customers, long accounts, long cards, long transactionTodays) {
		super();
		this.customers = customers;
		this.accounts = accounts;
		this.cards = cards;
		this.transactionTodays = transactionTodays;
	}

	public long getCustomers() {
		return customers;
	}

	public long getAccounts() {
		return accounts;
	}

	public long getCards() {
		return cards;
	}

	public long getTransactionTodays() {
		return transactionTodays;
	}
}
