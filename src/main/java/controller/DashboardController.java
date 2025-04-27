package controller;

import java.util.List;

import model.Transaction.TransactionStatus;
import model.dto.AdminDashboardStats;
import model.dto.PendingTransaction;
import model.dto.RecentTransaction;
import model.dto.TellerDashboardStats;
import service.DashboardService;

public class DashboardController {

	private DashboardService service;
	
	public DashboardController(DashboardService service) {
		this.service = service;
	}
	
	public AdminDashboardStats fetchAdminDashboardStats() {
		return service.getAdminDashboardStats();
	}
	
	public List<PendingTransaction> fetchAdminDashboardTable() {
		return service.getPendingTransactions();
	}
	
	public int updateTransactionStatus(int trxId, TransactionStatus status) {
		return service.changeTransactionStatus(trxId, status);
	}
	
	public TellerDashboardStats fetchTellerDashboardStats(int staffId) {
		return service.getTellerDashboardStats(staffId);
	}
	
	public List<RecentTransaction> fetchTellerDashboardTable() {
		return service.getRecentTransactions();
	}
	
	public List<RecentTransaction> fetchTellerDashboardTable(int staffId) {
		return service.getRecentTransactions(staffId);
	}
}
