package model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerAccount(
	int customerId,
	String name,
	String nrc,
	LocalDate dob,
	String email,
	String phone,
	String address,
	LocalDateTime createdAt,
	long numberOfAcc
//	List<AccNumberType> accNumberTypes
	) {
	
//	public record AccNumberType(String accNumber, String accType) {
//	}
	
	
	
}
