package controller;

import java.math.BigDecimal;
import java.util.List;

import model.Staff.Role;
import model.Transaction.TransactionType;
import model.dto.DepositWithdrawForm;
import model.dto.TransactionDetail;
import service.TransactionService;

public class TransactionController {

	private TransactionService service;

	public TransactionController(TransactionService service) {
		this.service = service;
	}

	public DepositWithdrawForm fetchAccountAndCustomer(String accountNumber) {
		return service.getDepositWithdrawForm(accountNumber);
	}
	
	public int depositBalance(BigDecimal amount, int accountId, int processedBy) {
		return service.updateBalance(amount, TransactionType.DEPOSIT, 0, accountId, processedBy);
	}

	public int withdrawBalance(BigDecimal amount, int accountId, int processedBy) {
		return service.updateBalance(amount, TransactionType.WITHDRAW, accountId, 0, processedBy);
	}
	
	public int transfer(int fromAccountId, int toAccountId, BigDecimal amount, int processedBy) {
		return service.updateBalance(amount, TransactionType.TRANSFER, fromAccountId, toAccountId, processedBy);
	}
	
	public List<TransactionDetail> fetchAllTransactionDetails(int loggedInStaffId, Role role) {
		return service.getAllTransactionDetails(loggedInStaffId, role);
	}
}
