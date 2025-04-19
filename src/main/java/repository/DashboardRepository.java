package repository;

import java.math.BigDecimal;
import java.util.List;

import config.DatabaseConfig;
import model.Transaction;

public class DashboardRepository {

	private DatabaseConfig db;
	
	public DashboardRepository() {
		db = new DatabaseConfig();
	}
	
	public int countCustomers() {
		
		return 0;
	}
	
	public int countAccounts() {
		
		return 0;
	}
	
	public BigDecimal calculateTotalBalance() {
		
		return new BigDecimal(23456.50);
	}
	
	public int countPendingTransactions() {
		
		return 0;
	}
	
	public List<Transaction> getPendingTransactions() {
		
		return null;
	}
}
