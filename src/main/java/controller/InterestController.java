package controller;

import java.math.BigDecimal;

import model.AccountType.Type;
import service.InterestService;

public class InterestController {
	
	private InterestService service;

	public InterestController(InterestService service) {
		this.service = service;
	}
	
	public int changeInterestRate(Type accountType, BigDecimal rate) {
		return service.updateInterestRate(accountType, rate);
	}
	
	public BigDecimal findInterestRate(Type accountType) {
		return service.getInterestRate(accountType);
	}
	
	public boolean isSameInterestRate(Type accountType, BigDecimal rate) {
		return service.checkDuplicateInterest(accountType, rate);
	}
	
	public long findSavingCount() {
		return service.getSavingCount();
	}
	
	public int updateInterestToAllActiveSaving(int processedBy) {
		return service.calculateInterestForAllSaving(processedBy);
	}
}
