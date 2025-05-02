package repository;

import model.Customer;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class CardRepository {

	public Customer findCustomerByAccountNumber(String accountNumber) {
        Customer customer = null;

        String sql = "SELECT c.customer_id, c.name, c.nrc, c.dob, c.email, c.phone, c.address " +
                     "FROM customers c " +
                     "JOIN customer_accounts ca ON c.customer_id = ca.customer_id " +
                     "JOIN accounts a ON ca.account_id = a.account_id " +
                     "WHERE a.account_number = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setId(rs.getInt("customer_id")); // corrected field name
                customer.setName(rs.getString("name"));
                customer.setNrc(rs.getString("nrc"));
                customer.setDob(rs.getObject("dob", LocalDate.class));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
	}
	public int findAccountIdByAccountNumber(String accountNumber) {
	    int accountId = 0;

	    String sql = "SELECT account_id FROM accounts WHERE account_number = ?";

	    try (Connection conn = DatabaseConfig.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, accountNumber);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            accountId = rs.getInt("account_id");
	        }

	        rs.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return accountId;
	}


	public boolean saveCard(int accountId, String cardNumber, String cardType,String status) {
		String sql = "INSERT INTO cards (account_id, card_number, card_type, issued_date, expiry_date,status, issued_by) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, accountId);
			stmt.setString(2, cardNumber);
			stmt.setString(3, cardType);
			stmt.setObject(4, java.time.LocalDate.now());
			stmt.setObject(5, java.time.LocalDate.now().plusYears(5));
			stmt.setString(6, status);
			stmt.setInt(7, 6);

			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public List<Object[]> findAllCards() {
	    List<Object[]> cards = new ArrayList<>();

	    String sql = "SELECT c.card_number, a.account_number, cu.name, cu.email, cu.phone, c.card_type, c.issued_date, c.expiry_date,c.status " +
	                 "FROM cards c " +
	                 "JOIN accounts a ON c.account_id = a.account_id " +
	                 "JOIN customer_accounts ca ON a.account_id = ca.account_id " +
	                 "JOIN customers cu ON ca.customer_id = cu.customer_id";

	    try (Connection conn = DatabaseConfig.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Object[] row = {
	                rs.getString("card_number"),
	                rs.getString("account_number"),
	                rs.getString("name"),
	                rs.getString("email"),
	                rs.getString("phone"),
	                rs.getString("card_type"),
	                rs.getDate("issued_date"),  // Issued Date
	                rs.getDate("expiry_date"),
	                rs.getString("status") 
	            };
	            cards.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return cards;
	}
	public long countCardsByStaffId(int staffId) {
		final String sql = "SELECT COUNT(*) FROM cards where issued_by = ?";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, staffId);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch cards count");
		}
		return 0;
	}

	
	public List<Object[]> searchCards(String keyword) {
		List<Object[]> cards = new ArrayList<>();

		String sql = "SELECT c.card_number, a.account_number, cu.name, cu.email, cu.phone, c.card_type, c.issued_date, c.expiry_date, c.status " +
		             "FROM cards c " +
		             "JOIN accounts a ON c.account_id = a.account_id " +
		             "JOIN customer_accounts ca ON a.account_id = ca.account_id " +
		             "JOIN customers cu ON ca.customer_id = cu.customer_id " +
		             "WHERE c.card_number LIKE ? OR a.account_number LIKE ? OR cu.name LIKE ?";

		try (Connection conn = DatabaseConfig.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {

			String searchPattern = "%" + keyword + "%";
			stmt.setString(1, searchPattern);
			stmt.setString(2, searchPattern);
			stmt.setString(3, searchPattern);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Object[] row = {
					rs.getString("card_number"),
					rs.getString("account_number"),
					rs.getString("name"),
					rs.getString("email"),
					rs.getString("phone"),
					rs.getString("card_type"),
					rs.getDate("issued_date"),
					rs.getDate("expiry_date"),
					rs.getString("status")
				};
				cards.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cards;
	}

}
