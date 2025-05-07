package repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;
import model.Transaction.TransactionStatus;
import model.Transaction.TransactionType;
import model.dto.RecentTransaction;
import model.dto.TransactionDetail;
import utils.ReferenceNumberGenerator;

public class TransactionRepository {

	private ReferenceNumberGenerator rnGenerator;

	public TransactionRepository(ReferenceNumberGenerator rnGenerator) {
		this.rnGenerator = rnGenerator;
	}

	public long countPendingTransactions() {
		final String sql = "SELECT COUNT(*) FROM transactions where status = 'PENDING'";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getLong(1));
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch Pending transactions count");
		}
		return 0L;
	}

	public int updateTransactionStatus(int trxId, TransactionStatus status) {
		final String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, status.toString());
			stmt.setInt(2, trxId);
			int row = stmt.executeUpdate();
			return row;
		} catch (Exception e) {
			System.out.println("Failed to fetch Pending transactions count");
		}
		return 0;
	}

	public List<RecentTransaction> searchLast24HrTransactions() {
		final String sql = """
				SELECT
				    t.reference_number,
				    c.name AS customer_name,
				    t.transaction_type,
				    t.amount,
				    t.transaction_date,
				    t.processed_by
				FROM
				    transactions t
				JOIN
				    accounts a ON t.from_account_id = a.account_id OR t.to_account_id = a.account_id
				JOIN
				    customer_accounts ca ON a.account_id = ca.account_id
				JOIN
				    customers c ON ca.customer_id = c.customer_id
				WHERE
					transaction_date >= NOW() - INTERVAL 24 HOUR;
								""";
		List<RecentTransaction> recents = new ArrayList<>();
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			while (rs.next()) {
				String referenceNumber = rs.getString(1);
				String cusName = rs.getString(2);
				TransactionType trxType = TransactionType.valueOf(rs.getString(3));
				BigDecimal amount = rs.getBigDecimal(4);
				LocalDateTime trxTime = rs.getTimestamp(5).toLocalDateTime();
				int processedBy = rs.getInt(6);
				recents.add(new RecentTransaction(referenceNumber, cusName, trxType, amount, trxTime, processedBy));
			}
			return recents;
		} catch (Exception e) {
			System.out.println("Failed to fetch Recent transactions");
		}
		return null;
	}

	public List<RecentTransaction> searchLast24HrTransactionsByStaffId(int staffId) {
		final String sql = """
				SELECT
				    t.reference_number,
				    c.name AS customer_name,
				    t.transaction_type,
				    t.amount,
				    t.transaction_date,
				    t.processed_by
				FROM
				    transactions t
				JOIN
				    accounts a ON t.from_account_id = a.account_id OR t.to_account_id = a.account_id
				JOIN
				    customer_accounts ca ON a.account_id = ca.account_id
				JOIN
				    customers c ON ca.customer_id = c.customer_id
				WHERE
					transaction_date >= NOW() - INTERVAL 24 HOUR and processed_by = ?;
								""";
		List<RecentTransaction> recents = new ArrayList<>();
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, staffId);
			var rs = stmt.executeQuery();
			while (rs.next()) {
				String referenceNumber = rs.getString(1);
				String cusName = rs.getString(2);
				TransactionType trxType = TransactionType.valueOf(rs.getString(3));
				BigDecimal amount = rs.getBigDecimal(4);
				LocalDateTime trxTime = rs.getTimestamp(5).toLocalDateTime();
				int processedBy = rs.getInt(6);
				recents.add(new RecentTransaction(referenceNumber, cusName, trxType, amount, trxTime, processedBy));
			}
			return recents;
		} catch (Exception e) {
			System.out.println("Failed to fetch Recent transactions for indidual staff");
		}
		return null;
	}

	public long countTodayTransactions() {
		final String sql = """
				SELECT
				    COUNT(*)
				FROM
				    transactions t
				JOIN
				    accounts a ON t.from_account_id = a.account_id OR t.to_account_id = a.account_id
				JOIN
				    customer_accounts ca ON a.account_id = ca.account_id
				JOIN
				    customers c ON ca.customer_id = c.customer_id
				WHERE
					DATE(transaction_date) = CURDATE();
								""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch today transactions count");
		}
		return 0;
	}

	public long countTodayTransactionsByStaffId(int staffId) {
		final String sql = """
				SELECT
				    COUNT(*)
				FROM
				    transactions t
				JOIN
				    accounts a ON t.from_account_id = a.account_id OR t.to_account_id = a.account_id
				JOIN
				    customer_accounts ca ON a.account_id = ca.account_id
				JOIN
				    customers c ON ca.customer_id = c.customer_id
				WHERE
					DATE(transaction_date) = CURDATE() and processed_by = ?;
								""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, staffId);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch today transactions count");
		}
		return 0;
	}

	public int addDepositTransaction(int toAccountId, TransactionType type, BigDecimal amount, int processedBy) {
		final String sql = """
				INSERT INTO transactions
				(to_account_id, transaction_type, amount, reference_number, processed_by) VALUES
				(?, ?, ?, ?, ?);
				""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, toAccountId);
			stmt.setString(2, type.toString());
			stmt.setBigDecimal(3, amount);
			stmt.setString(4, rnGenerator.generateNewReferenceNumber());
			stmt.setInt(5, processedBy);
			int row = stmt.executeUpdate();
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int addWithdrawTransaction(int fromAccountId, TransactionType type, BigDecimal amount, int processedBy) {
		final String sql = """
				INSERT INTO transactions
				(from_account_id, transaction_type, amount, reference_number, processed_by) VALUES
				(?, ?, ?, ?, ?);
				""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, fromAccountId);
			stmt.setString(2, type.toString());
			stmt.setBigDecimal(3, amount);
			stmt.setString(4, rnGenerator.generateNewReferenceNumber());
			stmt.setInt(5, processedBy);
			int row = stmt.executeUpdate();
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int addTransferTransaction(int fromAccountId, int toAccountId, TransactionType type, BigDecimal amount,
			int processedBy) {
		final String sql = """
				INSERT INTO transactions
				(from_account_id, to_account_id, transaction_type, amount, reference_number, processed_by) VALUES
				(?, ?, ?, ?, ?, ?);
				""";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, fromAccountId);
			stmt.setInt(2, toAccountId);
			stmt.setString(3, type.toString());
			stmt.setBigDecimal(4, amount);
			stmt.setString(5, rnGenerator.generateNewReferenceNumber());
			stmt.setInt(6, processedBy);
			int row = stmt.executeUpdate();
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<TransactionDetail> findAllTransactionDetails() {
		final String sql = """
				SELECT
				    t.reference_number,
				    t.transaction_date,
				    t.transaction_type,
				    t.amount,
				    a1.account_number AS from_account_number,
				    a2.account_number AS to_account_number,
				    s.username,
				    t.processed_by
				FROM
				    transactions t
				LEFT JOIN
				    accounts a1 ON t.from_account_id = a1.account_id
				LEFT JOIN
				    accounts a2 ON t.to_account_id = a2.account_id
				LEFT JOIN
				    staffs s ON t.processed_by = s.staff_id
				ORDER BY
					transaction_date DESC;
								""";
		List<TransactionDetail> transactionDetails = new ArrayList<>();
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			while(rs.next()) {
				TransactionDetail detail = new TransactionDetail(
											rs.getString("reference_number"), 
											rs.getTimestamp("transaction_date").toLocalDateTime(),
											TransactionType.valueOf(rs.getString("transaction_type")),
											rs.getBigDecimal("amount"),
											rs.getString("from_account_number"),
											rs.getString("to_account_number"),
											rs.getString("username") + " (ID: " + rs.getInt("processed_by") + ")");
				transactionDetails.add(detail);
			}
			return transactionDetails;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<TransactionDetail> findTransactionDetailsByStaffId(int loggedInStaffId) {
		final String sql = """
				SELECT
				    t.reference_number,
				    t.transaction_date,
				    t.transaction_type,
				    t.amount,
				    a1.account_number AS from_account_number,
				    a2.account_number AS to_account_number,
				    s.username,
				    t.processed_by
				FROM
				    transactions t
				LEFT JOIN
				    accounts a1 ON t.from_account_id = a1.account_id
				LEFT JOIN
				    accounts a2 ON t.to_account_id = a2.account_id
				LEFT JOIN
				    staffs s ON t.processed_by = s.staff_id
				WHERE 
					s.staff_id = ?
				ORDER BY
					transaction_date DESC;
								""";
		List<TransactionDetail> transactionDetails = new ArrayList<>();
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, loggedInStaffId);
			var rs = stmt.executeQuery();
			while(rs.next()) {
				TransactionDetail detail = new TransactionDetail(
											rs.getString("reference_number"), 
											rs.getTimestamp("transaction_date").toLocalDateTime(),
											TransactionType.valueOf(rs.getString("transaction_type")),
											rs.getBigDecimal("amount"),
											rs.getString("from_account_number"),
											rs.getString("to_account_number"),
											rs.getString("username") + " (ID: " + rs.getInt("processed_by") + ")");
				transactionDetails.add(detail);
			}
			return transactionDetails;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
