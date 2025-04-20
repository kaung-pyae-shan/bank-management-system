package repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;
import model.Transaction.TransactionStatus;
import model.Transaction.TransactionType;
import model.dto.PendingTransaction;

public class DashboardRepository {

	public long countCustomers() {
		final String sql = "SELECT COUNT(*) FROM customers";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch customer count");
		}
		return 0L;
	}

	public long countAccounts() {
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
		return 0L;
	}

	public BigDecimal calculateTotalBalance() {
		final String sql = "SELECT SUM(balance) FROM accounts";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getLong(1));
				return rs.getBigDecimal(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch Pending transactions count");
		}
		return new BigDecimal(23456.50);
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

	public List<PendingTransaction> searchPendingTransactions() {
		final String sql = """
				SELECT
				    t.transaction_id,
				    c.name AS customer_name,
				    t.transaction_type,
				    t.amount,
				    t.transaction_date
				FROM
				    transactions t
				JOIN
				    accounts a ON t.from_account_id = a.account_id OR t.to_account_id = a.account_id
				JOIN
				    customer_accounts ca ON a.account_id = ca.account_id
				JOIN
				    customers c ON ca.customer_id = c.customer_id
				WHERE
				    t.status = 'PENDING';
								""";
		List<PendingTransaction> pendings = new ArrayList<>();
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			var rs = stmt.executeQuery();
			while(rs.next()) {
				int trxId = rs.getInt(1);
				String cusName = rs.getString(2);
				TransactionType trxType = TransactionType.valueOf(rs.getString(3));
				BigDecimal amount = rs.getBigDecimal(4);
				LocalDateTime trxTime = rs.getTimestamp(5).toLocalDateTime();
				pendings.add(new PendingTransaction(trxId, cusName, trxType, amount, trxTime));
			}
			return pendings;
		} catch (Exception e) {
			System.out.println("Failed to fetch Pending transactions count");
		}
		return null;
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
}
