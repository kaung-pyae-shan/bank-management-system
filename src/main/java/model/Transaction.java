package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

	private int id;
	private Account fromAccount;
	private Account toAccount;
	private TransactionType transactionType;
	private BigDecimal amount;
	private LocalDateTime transactionDate;
	private TransactionStatus transactionStatus;
	private String referenceNumber;
	private Staff processedBy;
	private Staff handledBy;

	public enum TransactionType {
		DEPOSIT, WITHDRAW, TRANSFER, INTEREST, INITIAL
	}

	public enum TransactionStatus {
		PENDING, APPROVED, REJECTED
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Account getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Account getToAccount() {
		return toAccount;
	}

	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Staff getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(Staff processedBy) {
		this.processedBy = processedBy;
	}

	public Staff getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(Staff handledBy) {
		this.handledBy = handledBy;
	}

}
