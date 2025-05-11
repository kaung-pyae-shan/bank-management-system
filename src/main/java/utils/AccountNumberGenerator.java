package utils;

import config.DatabaseConfig;
import model.AccountType.OwnershipType;
import model.AccountType.Type;

public class AccountNumberGenerator {

	public static String generateNewAccountNumber(Type accoutType, OwnershipType ownershipType) {
		String lastAccountNumber = getLastAccountNumber(accoutType, ownershipType);
		String newNumber = null;

		if (lastAccountNumber != null) {
			long newNumberInt = Long.parseLong(lastAccountNumber) + 1; // Increment the last number
			newNumber = Long.toString(newNumberInt);
		} else {
//			11 for Saving
//			22 for Fixed
//			01 for Single
//			02 for Joint
			if(accoutType == Type.SAVING && ownershipType == OwnershipType.SINGLE) {
				newNumber = "110100000001";
			} else if(accoutType == Type.SAVING && ownershipType == OwnershipType.JOINT) {
				newNumber = "110200000001";				
			} else if(accoutType != Type.SAVING && ownershipType == OwnershipType.SINGLE) {
				newNumber = "220100000001";
			} else if(accoutType != Type.SAVING && ownershipType == OwnershipType.JOINT) {
				newNumber = "220200000001";
			} 
		}
		return newNumber;
	}

	private static String getLastAccountNumber(Type accoutType, OwnershipType ownershipType) {
        String sql = """
        		select a.* from accounts a
				join account_types at on a.account_type_id = at.account_type_id
				where account_type = ? and ownership_type = ?
				ORDER BY a.created_at DESC LIMIT 1
        		""";
        try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
        	stmt.setString(1, accoutType.toString());
        	stmt.setString(2, ownershipType.toString());
        	var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("account_number");
            }
        } catch (Exception e) {
			System.out.println("Failed to fetch last account number");
		}
        return null; // No previous ‌account number found
    }
}
