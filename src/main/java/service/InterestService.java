package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import model.Account.AccountStatus;
import model.AccountType.Type;
import model.Transaction.TransactionType;
import model.dto.FixedDetail;
import repository.AccountRepository;
import repository.InterestRepository;
import repository.TransactionRepository;

public class InterestService {

	private InterestRepository interestRepo;
	private AccountRepository accountRepo;
	private TransactionRepository transactionRepo;

	public InterestService(InterestRepository interestRepo, AccountRepository accountRepo,
			TransactionRepository transactionRepo) {
		this.transactionRepo = transactionRepo;
		this.accountRepo = accountRepo;
		this.interestRepo = interestRepo;
	}

	public int updateInterestRate(Type accountType, BigDecimal rate) {
		return interestRepo.updateInterestRate(accountType, rate);
	}

	public BigDecimal getInterestRate(Type accountType) {
		return interestRepo.searchInterestRateByAccountType(accountType);
	}

	public boolean checkDuplicateInterest(Type accountType, BigDecimal newRate) {
		BigDecimal rate = interestRepo.searchInterestRateByAccountType(accountType);
		if (rate.compareTo(newRate) == 0) {
			return true;
		}
		return false;
	}

	public long getSavingCount() {
		return accountRepo.searchSavingCount();
	}

	public int calculateInterestForAllSaving(int processedBy) {
//		interestPerMonth = [(previous_month_balance + DepositsBetweenDay1_5OfMonth) - (total_withdraw_this_month)] * [percent_per_annum / 12]
		int accountCount = 0;
		List<Integer> idList = accountRepo.getAllActiveSavingAccountId();
		int[] ids = idList.stream().mapToInt(Integer::intValue).toArray();
		for (int id : ids) {
			BigDecimal previousMonthBalance = accountRepo.getPreviousMonthBalance(id);
			BigDecimal d1to5Balance = getDay1to5Balance(id);
			BigDecimal withdrawBalance = transactionRepo.getThisMonthWithdrawAmountByAccountId(id);
			BigDecimal percentPerAnnum = interestRepo.searchInterestRateByAccountType(Type.SAVING);

			BigDecimal netBalance = previousMonthBalance.add(d1to5Balance).subtract(withdrawBalance);
			if(netBalance.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			BigDecimal monthlyInterestRate = percentPerAnnum.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
			
			accountCount += accountRepo.updateBalanceByAccountId(netBalance.multiply(monthlyInterestRate), true, id);
			transactionRepo.addDepositTransaction(id, TransactionType.INTEREST, netBalance.multiply(monthlyInterestRate), processedBy);
		}
		return accountCount;
	}

	private BigDecimal getDay1to5Balance(int accountId) {
		// Get the current year and month
		LocalDate currentDate = LocalDate.now();
		LocalDate lastMonthDate = currentDate.minusMonths(1);
		int year = lastMonthDate.getYear();
		int month = lastMonthDate.getMonthValue();

		LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0);
		LocalDateTime to = LocalDateTime.of(year, month, 5, 23, 59, 59);

		return transactionRepo.getDepositsDay1To5(accountId, from, to);
	}
	
	public void calculateInterestForAllFixed() {
		List<FixedDetail> fixedAccounts = accountRepo.getAllFixedAccountDetail();
		for(FixedDetail account : fixedAccounts) {
			BigDecimal durationBd = BigDecimal.valueOf(account.duration());
			BigDecimal percentPerAnnum = account.percentPerAnnum();
			if(!LocalDate.now().isBefore(account.dueDate()) && transactionRepo.countInerestTransactionsById(AccountStatus.ACTIVE, account.accountId()) == 0) {
				BigDecimal interest = account.balance()
						.multiply(percentPerAnnum.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP))
						.multiply(durationBd.divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP));
				accountRepo.updateBalanceByAccountId(interest, true, account.accountId());
				transactionRepo.addDepositTransaction(account.accountId(), TransactionType.INTEREST, interest, 0);
			}
		}
	}

}
