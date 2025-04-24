package service;

import java.math.BigDecimal;

import model.Transaction.TransactionType;
import model.dto.DepositWithdrawForm;
import repository.AccountRepository;

public class TransactionService {

	private AccountRepository accountRepo;

	public TransactionService(AccountRepository accountRepo) {
		this.accountRepo = accountRepo;
	}

	public DepositWithdrawForm getDepositWithdrawForm(String accountNumber) {
		return accountRepo.getDepositWithdrawFormByAccountNumber(accountNumber);
	}

	public int updateBalance(BigDecimal amount, TransactionType trxType, int fromAccountId, int toAccountid) {
		int row = 0;
		switch (trxType) {
		case DEPOSIT: {
			row = accountRepo.updateBalanceByAccountId(amount, true, toAccountid);
			return row;
			// transactions table logic here
		}
		case WITHDRAW: {
			row = accountRepo.updateBalanceByAccountId(amount, false, fromAccountId);
			return row;
			// transactions table logic here
		}
		case TRANSFER: {
			row += accountRepo.updateBalanceByAccountId(amount, true, toAccountid);
			row += accountRepo.updateBalanceByAccountId(amount, false, fromAccountId);
			return row;
			// transactions table logic here
		}
		case INTEREST: {

		}
		default:
			row = 0;
		}
		return row;
	}
}
