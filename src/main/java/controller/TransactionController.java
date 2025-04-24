package controller;

import java.math.BigDecimal;

import model.Transaction.TransactionType;
import model.dto.DepositWithdrawForm;
import service.TransactionService;

public class TransactionController {

	private TransactionService service;

	public TransactionController(TransactionService service) {
		this.service = service;
	}

	public DepositWithdrawForm fetchAccountAndCustomer(String accountNumber) {
		return service.getDepositWithdrawForm(accountNumber);
	}
	
	public int depositBalance(BigDecimal amount, int accountId) {
		return service.updateBalance(amount, TransactionType.DEPOSIT, 0, accountId);
	}

	public int withdrawBalance(BigDecimal amount, int accountId) {
		return service.updateBalance(amount, TransactionType.WITHDRAW, accountId, 0);
	}
	
	public int transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
		return service.updateBalance(amount, TransactionType.TRANSFER, fromAccountId, toAccountId);
	}
}
