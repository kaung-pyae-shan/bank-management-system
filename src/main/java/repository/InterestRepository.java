package repository;

import java.math.BigDecimal;
import java.sql.SQLException;

import config.DatabaseConfig;
import model.AccountType.Type;

public class InterestRepository {
	
	public int updateInterestRate(Type accountType, BigDecimal rate) {
		String sql = """
				UPDATE account_interest_rates air
				JOIN account_types at ON air.account_type_id = at.account_type_id
				SET air.interest_rate = ?
				WHERE at.account_type = ?
				""";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setBigDecimal(1, rate);
			stmt.setString(2, accountType.toString());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public BigDecimal searchInterestRateByAccountType(Type accountType) {
		String sql = """
				SELECT air.interest_rate
				FROM account_interest_rates air
				JOIN account_types at ON air.account_type_id = at.account_type_id
				WHERE at.account_type = ?
				""";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, accountType.toString());
			var rs = stmt.executeQuery();
			if (rs.next()) {
				var result = rs.getBigDecimal("interest_rate");
				return result == null ? new BigDecimal(0) : result;
			}
			return new BigDecimal(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}
	
	
	
}
