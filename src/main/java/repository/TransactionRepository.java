package repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;
import model.Transaction.TransactionStatus;
import model.Transaction.TransactionType;
import model.dto.DepositWithdrawForm;
import model.dto.PendingTransaction;
import model.dto.RecentTransaction;

public class TransactionRepository {

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
			System.out.println("Failed to fetch Pending transactions");
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
	
	public List<RecentTransaction> searchLast24HrTransactionsByStaffId(int staffId) {
		final String sql = """
				SELECT
				    t.transaction_id,
				    c.name AS customer_name,
				    t.transaction_type,
				    t.amount,
				    t.transaction_date,
				    t.status
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
			while(rs.next()) {
				int trxId = rs.getInt(1);
				String cusName = rs.getString(2);
				TransactionType trxType = TransactionType.valueOf(rs.getString(3));
				BigDecimal amount = rs.getBigDecimal(4);
				LocalDateTime trxTime = rs.getTimestamp(5).toLocalDateTime();
				TransactionStatus trxStatus = TransactionStatus.valueOf(rs.getString(6));
				recents.add(new RecentTransaction(trxId, cusName, trxType, amount, trxTime, trxStatus));
			}
			return recents;
		} catch (Exception e) {
			System.out.println("Failed to fetch Recent transactions");
		}
		return null;
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
}
