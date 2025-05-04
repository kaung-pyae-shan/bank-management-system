package repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;
import model.AccountType.Type;
import model.dto.DepositWithdrawForm;

public class AccountRepository {

	public long countTotalAccounts() {
		final String sql = "SELECT COUNT(*) FROM accounts";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
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
				SELECT a.account_id, at.account_type, a.account_number, name, email, phone, address, c.created_at, balance
				FROM accounts a
				JOIN customer_accounts ca on a.account_id = ca.account_id
				JOIN customers c on ca.customer_id = c.customer_id
				JOIN account_types at on a.account_type_id = at.account_type_id
				WHERE a.account_number = ?
				""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, accountNumber);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return new DepositWithdrawForm(rs.getInt(1), Type.valueOf(rs.getString(2)), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
						rs.getTimestamp(8).toLocalDateTime().toLocalDate(), rs.getBigDecimal(9));
			}
			return null;
		} catch (Exception e) {
			System.out.println("Failed to fetch Recent transactions");
		}
		return null;
	}

	public int updateBalanceByAccountId(BigDecimal amount, boolean isDeposit, int accountId) {
		String sql;
		if (isDeposit) {
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

	public long searchSavingCount() {
		String sql = """
				SELECT COUNT(*) AS saving_count FROM accounts a
				JOIN account_types at ON a.account_type_id = at.account_type_id
				WHERE at.account_type = 'SAVING';
				""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong("saving_count");
			}
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public BigDecimal getPreviousMonthBalance(int accountId) {
		String sql = "SELECT previous_month_balance FROM accounts WHERE account_id = ?";

		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, accountId);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				var result = rs.getBigDecimal("previous_month_balance");
				return result == null ? new BigDecimal(0) : result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new BigDecimal(0);
	}
	
	public int setPreviousMonthBalance(int accountId) {
		String sql = "UPDATE accounts SET previous_month_balance = balance WHERE account_id = ?";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, accountId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
//	public int setPreviousMonthBalance(int accountId, BigDecimal amount) {
//		String sql = "UPDATE accounts SET previous_month_balance = ? WHERE account_id = ?";
//
//		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
//			stmt.setBigDecimal(1, amount);
//			stmt.setInt(2, accountId);
//			return stmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	public List<Integer> getAllActiveSavingAccountId() {
		String sql = """
				SELECT a.account_id
				FROM accounts a
				JOIN account_types at ON a.account_type_id = at.account_type_id
				WHERE at.account_type = 'SAVING' AND a.status = 'ACTIVE';
				""";
		
		List<Integer> idList = new ArrayList<>();
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getInt("account_id"));
			}
			return idList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idList;
	}
}
