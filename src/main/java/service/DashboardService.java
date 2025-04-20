package service;

import java.util.List;

import model.Transaction.TransactionStatus;
import model.dto.AdminDashboardStats;
import model.dto.PendingTransaction;
import repository.DashboardRepository;

public class DashboardService {

	private DashboardRepository repo;
	
	public DashboardService() {
		repo = new DashboardRepository();
	}
	
	public AdminDashboardStats getAdminDashboardStats() {
		return new AdminDashboardStats(
				repo.countCustomers(),
				repo.countAccounts(),
				repo.calculateTotalBalance(),
				repo.countPendingTransactions());
	}

	public List<PendingTransaction> getPendingTransactions() {
		return repo.searchPendingTransactions();
	}
	
	public int changeTransactionStatus(int trxId, TransactionStatus status) {
		return repo.updateTransactionStatus(trxId, status);
	}
}
