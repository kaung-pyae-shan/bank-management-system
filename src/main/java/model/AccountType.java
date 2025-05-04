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
	
	  public String getTypeAsString() {
	        switch (type) {
	            case SAVING:
	                return "Saving Account";
	            case FIXED_90D:
	                return "Fixed 90 Days";
	            case FIXED_180D:
	                return "Fixed 180 Days";
	            case FIXED_360D:
	                return "Fixed 360 Days";
	            case FIXED_720D:
	                return "Fixed 720 Days";
	            case FIXED_1080D:
	                return "Fixed 1080 Days";
	            default:
	                return type.name(); // Default to raw enum name if not mapped
	        }
	    }

	    // Helper method to return the ownership type as a string
	    public String getOwnershipTypeAsString() {
	        switch (ownershipType) {
	            case SINGLE:
	                return "Single Owner";
	            case JOINT:
	                return "Joint Account";
	            default:
	                return ownershipType.name(); // Default to raw enum name if not mapped
	        }
	    }
}
