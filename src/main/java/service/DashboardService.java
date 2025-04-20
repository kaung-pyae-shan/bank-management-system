package service;

import java.util.List;

import model.Transaction.TransactionStatus;
import model.dto.AdminDashboardStats;
import model.dto.PendingTransaction;
import model.dto.RecentTransaction;
import model.dto.TellerDashboardStats;
import repository.DashboardRepository;

public class DashboardService {

	private DashboardRepository repo;
	
	public DashboardService() {
		repo = new DashboardRepository();
	}
	
	public AdminDashboardStats getAdminDashboardStats() {
		return new AdminDashboardStats(
				repo.countTotalCustomers(),
				repo.countTotalAccounts(),
				repo.calculateTotalBalance(),
				repo.countPendingTransactions());
	}

	public List<PendingTransaction> getPendingTransactions() {
		return repo.searchPendingTransactions();
	}
	
	public int changeTransactionStatus(int trxId, TransactionStatus status) {
		return repo.updateTransactionStatus(trxId, status);
	}
	
	public TellerDashboardStats getTellerDashboardStats(int staffId) {
		return new TellerDashboardStats(
				repo.countCustomersByStaffId(staffId),
				repo.countAccountsByStaffId(staffId),
				repo.countCardsByStaffId(staffId),
				repo.countTodayTransactionsByStaffId(staffId));
	}
	
	public List<RecentTransaction> getRecentTransactions(int staffId) {
		return repo.searchLast24HrTransactionsByStaffId(staffId);
	}
}
