package service;

import java.math.BigDecimal;
import java.util.List;

import model.Staff.Role;
import model.Transaction.TransactionType;
import model.dto.DepositWithdrawForm;
import model.dto.TransactionDetail;
import repository.AccountRepository;
import repository.TransactionRepository;

public class TransactionService {

	private TransactionRepository transactionRepo;
	private AccountRepository accountRepo;

	public TransactionService(TransactionRepository transactionRepo, AccountRepository accountRepo) {
		this.transactionRepo = transactionRepo;
		this.accountRepo = accountRepo;
	}

	public DepositWithdrawForm getDepositWithdrawForm(String accountNumber) {
		return accountRepo.getDepositWithdrawFormByAccountNumber(accountNumber);
	}

	public int updateBalance(BigDecimal amount, TransactionType trxType, int fromAccountId, int toAccountid, int processedBy) {
		int row = 0;
		switch (trxType) {
		case DEPOSIT: {
			row = accountRepo.updateBalanceByAccountId(amount, true, toAccountid);
			transactionRepo.addDepositTransaction(toAccountid, trxType, amount, processedBy);
			return row;
		}
		case WITHDRAW: {
			row = accountRepo.updateBalanceByAccountId(amount, false, fromAccountId);
			transactionRepo.addWithdrawTransaction(fromAccountId, trxType, amount, processedBy);
			return row;
		}
		case TRANSFER: {
			row += accountRepo.updateBalanceByAccountId(amount, true, toAccountid);
			row += accountRepo.updateBalanceByAccountId(amount, false, fromAccountId);
			transactionRepo.addTransferTransaction(fromAccountId, toAccountid, trxType, amount, processedBy);
			return row;
		}
		case INTEREST: {

		}
		default:
			row = 0;
		}
		return row;
	}
	
	public List<TransactionDetail> getAllTransactionDetails(int loggedInStaffId, Role role) {
		if(role == Role.ADMIN) {
			return transactionRepo.findAllTransactionDetails();			
		} else {
			return transactionRepo.findTransactionDetailsByStaffId(loggedInStaffId);
		}
	}
}
