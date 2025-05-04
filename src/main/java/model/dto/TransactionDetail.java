package model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.Transaction.TransactionType;

public record TransactionDetail(
		String referenceNo,
		LocalDateTime timestamp,
		TransactionType type,
		BigDecimal amount,
		String fromAccount,
		String toAccount,
		String performedBy
		) {

}
