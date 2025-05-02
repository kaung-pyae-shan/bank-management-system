package controller;

import java.util.List;

import model.Customer;
import model.dto.AccNumberType;
import service.CustomerService;

public class CustomerController {

	private CustomerService service;

	public CustomerController(CustomerService service) {
		this.service = service;
	}

	public List<Customer> fetchCustomers() {
		return service.getAllCustomers();
	}
	
	public Customer fetchCustomerByNrc(String nrc) {
		return service.getCustomerByNrc(nrc);
	}
	
	public Long fetchNumberOfAccounts(int customerId) {
		return service.getAccountCount(customerId);
	}

	public List<AccNumberType> fetchAccNumberAndType(int selectedCustomerId) {
		return service.getAccNumberType(selectedCustomerId);
	}
	
	public int addNewCustomer(Customer customer) {
		return service.addCustomer(customer);
	}
	
	public int editCustomerById(Customer customer) {
		return service.editCustomer(customer);
	}
}
