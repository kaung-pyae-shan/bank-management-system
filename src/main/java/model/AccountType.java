package model;

import java.math.BigDecimal;

public class AccountType {

	private int id;
	private Type type;
	private OwnershipType ownershipType;
	private BigDecimal minimumBalance;
	
	public enum Type {
		SAVING, FIXED_90D, FIXED_180D, FIXED_360D, FIXED_720D, FIXED_1080D
	}
	
	public enum OwnershipType {
		SINGLE, JOINT
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public OwnershipType getOwnershipType() {
		return ownershipType;
	}

	public void setOwnershipType(OwnershipType ownershipType) {
		this.ownershipType = ownershipType;
	}

	public BigDecimal getMinimumBalance() {
		return minimumBalance;
	}

	public void setMinimumBalance(BigDecimal minimumBalance) {
		this.minimumBalance = minimumBalance;
	}
}
