package model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.AccountType.Type;

public class DepositWithdrawForm {

	private int accountId;
	private Type accountType;
	private String accountNumber;
	private String accountHolder;
	private String email;
	private String phone;
	private String address;
	private LocalDate customerSince;
	private BigDecimal currentBalance;

	public DepositWithdrawForm(int accountId, Type accountType, String accountNumber, String accountHolder, String email, String phone,
			String address, LocalDate customerSince, BigDecimal currentBalance) {
		super();
		this.accountId = accountId;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.accountHolder = accountHolder;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.customerSince = customerSince;
		this.currentBalance = currentBalance;
	}

	public int getAccountId() {
		return accountId;
	}
	
	public Type getAccountType() {
		return accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public LocalDate getCustomerSince() {
		return customerSince;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}
}
