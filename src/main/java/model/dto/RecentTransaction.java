package model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.Transaction.TransactionStatus;
import model.Transaction.TransactionType;

public class RecentTransaction {

	private int trxId;
	private String customer;
	private TransactionType transactionType;
	private BigDecimal amount;
	private LocalDateTime date;
	private TransactionStatus status;

	public RecentTransaction(int trxId, String customer, TransactionType transactionType, BigDecimal amount, LocalDateTime date, TransactionStatus status) {
		super();
		this.trxId = trxId;
		this.customer = customer;
		this.transactionType = transactionType;
		this.amount = amount;
		this.date = date;
		this.status = status;
	}

	public int getTrxId() {
		return trxId;
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
	
	public TransactionStatus getStatus() {
		return status;
	}
	
	public Object[] toObject() {
		Object[] obj = {this.trxId, this.customer, this.transactionType, this.amount.toString(), this.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), this.status};
		return obj;
	}

}
