package config;

import controller.DashboardController;
import controller.TransactionController;
import repository.AccountRepository;
import repository.CardRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.DashboardService;
import service.TransactionService;
import utils.ReferenceNumberGenerator;

public class DependenciesConfig {
	
	// =============== Instantiate Utils ===============
	private ReferenceNumberGenerator rnGenerator = new ReferenceNumberGenerator();
	
	// =============== Instantiate repositories ===============
	private AccountRepository accountRepo = new AccountRepository();
	private CustomerRepository customerRepo = new CustomerRepository();
	private TransactionRepository transactionRepo = new TransactionRepository(rnGenerator);
	private CardRepository cardRepo = new CardRepository();
	
	// =============== Instantiate services ===============
	private DashboardService dashboardService = new DashboardService(customerRepo, accountRepo, transactionRepo, cardRepo);
	private TransactionService transactionService = new TransactionService(transactionRepo, accountRepo);
	
	// =============== Instantiate controllers ===============
	private DashboardController dashboardController = new DashboardController(dashboardService);
	private TransactionController transactionController = new TransactionController(transactionService);
	
	
	// =============== Getters for controllers ===============
	public DashboardController getDashboardController() {
		return dashboardController;
	}

	public TransactionController getTransactionController() {
		return transactionController;
	}
}
