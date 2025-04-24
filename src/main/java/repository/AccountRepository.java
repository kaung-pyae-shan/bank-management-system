package repository;

import java.math.BigDecimal;

import config.DatabaseConfig;
import model.dto.DepositWithdrawForm;

public class AccountRepository {

	public long countTotalAccounts() {
		final String sql = "SELECT COUNT(*) FROM accounts";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getLong(1));
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch account count");
		}
		return 0;
	}
	
	public BigDecimal calculateTotalBalance() {
		final String sql = "SELECT SUM(balance) FROM accounts";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getBigDecimal(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch totoal balance");
		}
		return null;
	}
	
	public long countAccountsByStaffId(int staffId) {
		final String sql = "SELECT COUNT(*) FROM accounts where created_by = ?";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, staffId);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch accounts count");
		}
		return 0;
	}
	
	public DepositWithdrawForm getDepositWithdrawFormByAccountNumber(String accountNumber) {
		final String sql = """
				SELECT a.account_id, a.account_number, name, email, phone, address, c.created_at, balance
				FROM accounts a
				JOIN customer_accounts ca on a.account_id = ca.account_id
				JOIN customers c on ca.customer_id = c.customer_id
				WHERE a.account_number = ?
				""";	
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, accountNumber);
			var rs = stmt.executeQuery();
			if(rs.next()) {
				return new DepositWithdrawForm(
						rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getTimestamp(7).toLocalDateTime().toLocalDate(),
						rs.getBigDecimal(8));
			}
			return null;
		} catch (Exception e) {
			System.out.println("Failed to fetch Recent transactions");
		}
		return null;
	}
	
	public int updateBalanceByAccountId(BigDecimal amount, boolean isDeposit, int accountId) {
		String sql;
		if(isDeposit) {
			sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
		} else {
			sql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
		}
		
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setBigDecimal(1, amount);
			stmt.setInt(2, accountId);
			return stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Failed to update balance");
		}
		return 0;
	}
}
