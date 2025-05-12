package model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.AccountType.Type;

public record FixedDetail(
		int accountId,
		BigDecimal balance,
		Type accountType,
		LocalDate dueDate,
		BigDecimal percentPerAnnum,
		int duration
		) {

}
