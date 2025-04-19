package service;

import model.dto.AdminDashboardStats;
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
}
