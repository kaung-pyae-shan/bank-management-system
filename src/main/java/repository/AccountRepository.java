package repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.DatabaseConfig;
import model.Customer;
import model.Account;
import model.AccountType.Type;
import model.Staff;
import model.AccountType;
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

	public int insertAccount(Account account, List<Integer> customerIds) {
		final String accountSql = "INSERT INTO accounts (account_type_id, account_number, balance, status, created_at, created_by) VALUES (?, ?, ?, ?, ?, ?)";
		final String linkSql = "INSERT INTO customer_accounts (account_id, customer_id) VALUES (?, ?)";

		try (var con = DatabaseConfig.getConnection();
				var accountStmt = con.prepareStatement(accountSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
			// Disable auto-commit to manage transaction manually
			con.setAutoCommit(false);

			// Insert into accounts table
			accountStmt.setInt(1, account.getAccountType().getId());
			accountStmt.setString(2, account.getAccountNumber());
			accountStmt.setBigDecimal(3, account.getBalance());
			accountStmt.setString(4, account.getStatus().name());
			accountStmt.setTimestamp(5, java.sql.Timestamp.valueOf(account.getCreatedAt()));
			accountStmt.setInt(6, account.getCreatedBy().getId());

			int affectedRows = accountStmt.executeUpdate();
			if (affectedRows == 0) {
				con.rollback();
				throw new SQLException("Inserting account failed, no rows affected.");
			}

			// Get generated account_id
			int accountId;
			try (var generatedKeys = accountStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					accountId = generatedKeys.getInt(1);
				} else {
					con.rollback();
					throw new SQLException("Inserting account failed, no ID obtained.");
				}
			}

			// Insert into customer_accounts table
			try (var linkStmt = con.prepareStatement(linkSql)) {
				for (int customerId : customerIds) {
					linkStmt.setInt(1, accountId);
					linkStmt.setInt(2, customerId);
					linkStmt.addBatch();
				}
				linkStmt.executeBatch();
			}

			// Commit transaction
			con.commit();
			return accountId;

		} catch (Exception e) {
			e.printStackTrace(); // Or use proper logging
		}
		return -1;
	}

	public List<Account> getAllAccounts() {
		String sql = """
					            SELECT a.account_id,
					            	   a.account_number,
					                   at.account_type AS type,
					                   at.ownership_type,
					                   a.balance,
					                   a.status,
					                   at.minimum_balance
					            FROM accounts a
				JOIN account_types at ON a.account_type_id = at.account_type_id
					    """;

		List<Account> accounts = new ArrayList<>();

		// Use explicit types instead of `var`
		try (Connection con = DatabaseConfig.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Account account = new Account();
				account.setId(rs.getInt("account_id"));
				account.setAccountNumber(rs.getString("account_number"));
				account.setBalance(rs.getBigDecimal("balance"));
				account.setStatus(Account.AccountStatus.valueOf(rs.getString("status")));

				// Map AccountType details
				AccountType accountType = new AccountType();
				accountType.setType(AccountType.Type.valueOf(rs.getString("type")));
				accountType.setOwnershipType(AccountType.OwnershipType.valueOf(rs.getString("ownership_type")));
				accountType.setMinimumBalance(rs.getBigDecimal("minimum_balance"));

				// Set AccountType to Account
				account.setAccountType(accountType);

				accounts.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log SQL exception for debugging
		}
		return accounts;
	}

	public long countCustomersByAccount(int accountId) {
		final String sql = """
				SELECT COUNT(ca.customer_id) AS customer_count
				FROM customer_accounts ca
				WHERE ca.account_id = ?
				GROUP BY ca.account_id;
				""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, accountId);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch customers count");
		}
		return 0;
	}
	
	public List<Integer> getCustomerIdsByAccountNumber(String accNumber) {
	    List<Integer> customerIds = new ArrayList<>();
	    String sql = "SELECT customer_id FROM customer_accounts WHERE account_id = (SELECT account_id FROM accounts WHERE account_number = ?)";

	    try (Connection conn = DatabaseConfig.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, accNumber);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            customerIds.add(rs.getInt("customer_id"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return customerIds;
	}
	public boolean updateAccount(Account account, List<Integer> customerIds) {
	    String updateAccountSql = """
	        UPDATE accounts 
	        SET balance = ?, status = ?, account_type_id = ?
	        WHERE account_number = ?
	    """;

	    String deleteCustomerMapSql = "DELETE FROM customer_accounts WHERE account_id = ?";
	    String insertCustomerMapSql = "INSERT INTO customer_accounts (account_id, customer_id) VALUES (?, ?)";

	    try (Connection conn = DatabaseConfig.getConnection()) {
	        conn.setAutoCommit(false);

	        // Get the account ID first
	        int accountId = getAccountIdByAccountNumber(account.getAccountNumber(), conn);
	        if (accountId == -1) {
	            return false;
	        }

	        // Update account table
	        try (PreparedStatement stmt = conn.prepareStatement(updateAccountSql)) {
	            stmt.setBigDecimal(1, account.getBalance());
	            stmt.setString(2, account.getStatus().name());
	            stmt.setInt(3, account.getAccountType().getId());
	            stmt.setString(4, account.getAccountNumber());
	            stmt.executeUpdate();
	        }

	        // Remove old customer mappings
	        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteCustomerMapSql)) {
	            deleteStmt.setInt(1, accountId);
	            deleteStmt.executeUpdate();
	        }

	        // Add new customer mappings
	        try (PreparedStatement insertStmt = conn.prepareStatement(insertCustomerMapSql)) {
	            for (Integer customerId : customerIds) {
	                insertStmt.setInt(1, accountId);
	                insertStmt.setInt(2, customerId);
	                insertStmt.addBatch();
	            }
	            insertStmt.executeBatch();
	        }

	        conn.commit();
	        return true;

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}


	private int getAccountIdByAccountNumber(String accountNumber, Connection conn) throws SQLException {
	    String sql = "SELECT account_id FROM accounts WHERE account_number = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, accountNumber);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("account_id");
	            }
	        }
	    }
	    return -1;
	}

}
