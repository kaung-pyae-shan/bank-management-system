package service;

import java.util.List;

import model.Customer;
import model.dto.AccNumberType;
import repository.CustomerRepository;

public class CustomerService {
	
	private CustomerRepository customerRepo;

	public CustomerService(CustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	
	public List<Customer> getAllCustomers() {
		return customerRepo.findAllCustomers();
	}

	public long getAccountCount(int customerId) {
		return customerRepo.getAccountsCountByCustomerId(customerId);
	}
	
	public List<AccNumberType> getAccNumberType(int customerId) {
		return customerRepo.getAccNumberTypesByCustomerId(customerId);
	}
	
	public int addCustomer(Customer customer) {
		return customerRepo.insertCustomer(customer);
	}
	
	public int editCustomer(Customer customer) {
		return customerRepo.updateCustomer(customer);
	}

	public Customer getCustomerByNrc(String nrc) {
		return customerRepo.findCustomerByNRc(nrc);
	}
}
