package controller;

import java.util.List;

import model.Transaction.TransactionStatus;
import model.dto.AdminDashboardStats;
import model.dto.PendingTransaction;
import service.DashboardService;

public class DashboardController {

	private DashboardService service;
	
	public DashboardController() {
		service = new DashboardService();
	}
	
	public AdminDashboardStats fetchAdminDashboardStats() {
		return service.getAdminDashboardStats();
	}
	
	public List<PendingTransaction> FetchAdminDashboardTable() {
		return service.getPendingTransactions();
	}
	
	public int updateTransactionStatus(int trxId, TransactionStatus status) {
		return service.changeTransactionStatus(trxId, status);
	}
}
