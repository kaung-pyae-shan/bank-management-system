package config;

import controller.CustomerController;
import controller.DashboardController;
import controller.InterestController;
import controller.LoginController;
import controller.TransactionController;
import repository.AccountRepository;
import repository.CardRepository;
import repository.CustomerRepository;
import repository.InterestRepository;
import repository.StaffRepository;
import repository.TransactionRepository;
import service.CustomerService;
import service.DashboardService;
import service.InterestService;
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
	private InterestRepository interestRepo = new InterestRepository();
	
	// =============== Instantiate services ===============
	private DashboardService dashboardService = new DashboardService(customerRepo, accountRepo, transactionRepo, cardRepo);
	private TransactionService transactionService = new TransactionService(transactionRepo, accountRepo);
	private CustomerService customerService = new CustomerService(customerRepo);
	private LoginService loginService = new LoginService(staffRepo);
	private InterestService interestService = new InterestService(interestRepo, accountRepo, transactionRepo);
	
	// =============== Instantiate controllers ===============
	private DashboardController dashboardController = new DashboardController(dashboardService);
	private TransactionController transactionController = new TransactionController(transactionService);
	private CustomerController customerController = new CustomerController(customerService);
	private LoginController loginController = new LoginController(loginService);
	private InterestController interestController = new InterestController(interestService);
	
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
	
	public InterestController getInterestController() {
		return interestController;
	}
}
