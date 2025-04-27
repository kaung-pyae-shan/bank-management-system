package model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.Transaction.TransactionType;

public class RecentTransaction {

	private String referenceNumber;
	private String customer;
	private TransactionType transactionType;
	private BigDecimal amount;
	private LocalDateTime date;
	private int processedBy;

	public RecentTransaction(String referenceNumber, String customer, TransactionType transactionType, BigDecimal amount, LocalDateTime date, int processedBy) {
		super();
		this.referenceNumber = referenceNumber;
		this.customer = customer;
		this.transactionType = transactionType;
		this.amount = amount;
		this.date = date;
		this.processedBy = processedBy;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public String getCustomer() {
		return customer;
	}
	
	public TransactionType getTransactionType() {
		return transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public LocalDateTime getDate() {
		return date;
	}
	
	public int getProcessedBy() {
		return processedBy;
	}
	
	public Object[] toObject() {
		Object[] obj = {this.referenceNumber, this.customer, this.transactionType, this.amount.toString(), this.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), this.processedBy};
		return obj;
	}

}
