package config;

import controller.CustomerController;
import controller.DashboardController;
import controller.LoginController;
import controller.TransactionController;
import repository.AccountRepository;
import repository.CardRepository;
import repository.CustomerRepository;
import repository.StaffRepository;
import repository.TransactionRepository;
import service.CustomerService;
import service.DashboardService;
import service.LoginService;
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
	private StaffRepository staffRepo = new StaffRepository();
	
	// =============== Instantiate services ===============
	private DashboardService dashboardService = new DashboardService(customerRepo, accountRepo, transactionRepo, cardRepo);
	private TransactionService transactionService = new TransactionService(transactionRepo, accountRepo);
	private CustomerService customerService = new CustomerService(customerRepo);
	private LoginService loginService = new LoginService(staffRepo);
	
	// =============== Instantiate controllers ===============
	private DashboardController dashboardController = new DashboardController(dashboardService);
	private TransactionController transactionController = new TransactionController(transactionService);
	private CustomerController customerController = new CustomerController(customerService);
	private LoginController loginController = new LoginController(loginService);
	
	// =============== Getters for controllers ===============
	public DashboardController getDashboardController() {
		return dashboardController;
	}

	public TransactionController getTransactionController() {
		return transactionController;
	}
	
	public CustomerController getCustomerController() {
		return customerController;
	}
	
	public LoginController getLoginController() {
		return loginController;
	}
}
