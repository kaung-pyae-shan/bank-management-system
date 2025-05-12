package service;

import java.util.List;

import model.Transaction.TransactionStatus;
import model.dto.AdminDashboardStats;
import model.dto.RecentTransaction;
import model.dto.TellerDashboardStats;
import model.dto.TransactionDetail;
import repository.AccountRepository;
import repository.CardRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;

public class DashboardService {

	private CustomerRepository customerRepo;
	private AccountRepository accountRepo;
	private TransactionRepository transactionRepo;
	private CardRepository cardRepo;
	
	public DashboardService(CustomerRepository customerRepo, AccountRepository accountRepo, TransactionRepository transactionRepo, CardRepository cardRepo) {
		this.customerRepo = customerRepo;
		this.accountRepo = accountRepo;
		this.transactionRepo = transactionRepo;
		this.cardRepo = cardRepo;
	}
	
	public AdminDashboardStats getAdminDashboardStats() {
		return new AdminDashboardStats(
				customerRepo.countTotalCustomers(),
				accountRepo.countTotalAccounts(),
				accountRepo.calculateTotalBalance(),
				transactionRepo.countTodayTransactions());
	}
	
	public int changeTransactionStatus(int trxId, TransactionStatus status) {
		return transactionRepo.updateTransactionStatus(trxId, status);
	}
	
	public TellerDashboardStats getTellerDashboardStats(int staffId) {
		return new TellerDashboardStats(
				customerRepo.countCustomersByStaffId(staffId),
				accountRepo.countAccountsByStaffId(staffId),
				cardRepo.countCardsByStaffId(staffId),
				transactionRepo.countTodayTransactionsByStaffId(staffId));
	}
	
	public List<TransactionDetail> getRecentTransactions() {
		return transactionRepo.findAllTransactionDetailsLast24Hour();
	}
	
	public List<TransactionDetail> getRecentTransactions(int staffId) {
		return transactionRepo.findTransactionDetailsByStaffIdLast24Hour(staffId);
	}
}
