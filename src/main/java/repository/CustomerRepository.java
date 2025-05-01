package repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;
import model.Customer;
import model.dto.AccNumberType;

public class CustomerRepository {

	public long countTotalCustomers() {
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

	public long countCustomersByStaffId(int staffId) {
		final String sql = "SELECT COUNT(*) FROM customers where created_by = ?";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, staffId);
			var rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch customers count");
		}
		return 0;
	}

	public List<Customer> findAllCustomers() {
		String sql = "SELECT * FROM customers";
		try (var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {

			List<Customer> customers = new ArrayList<>();

			var rs = stmt.executeQuery();
			while (rs.next()) {
				int customerId = rs.getInt("customer_id");
				String name = rs.getString("name");
				String nrc = rs.getString("nrc");
				LocalDate dob = rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null;
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				String address = rs.getString("address");
				LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
				
				Customer customer = new Customer(customerId, name, dob, nrc, email, phone, address, createdAt, null);
				customers.add(customer);
			}
			return customers;
		} catch (Exception e) {
			System.out.println("Failed to fetch customers");
		}
		return null;
	}
	
	public long getAccountsCountByCustomerId(int customerId) {
		String sql = """
				SELECT COUNT(a.account_id) AS num_of_accounts
				FROM customers c
				JOIN customer_accounts ca ON c.customer_id = ca.customer_id
				JOIN accounts a on ca.account_id = a.account_id
				WHERE c.customer_id = ?;
				""";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, customerId);
			var rs = stmt.executeQuery();
			long count = 0;
			if(rs.next()) {
				count = rs.getLong("num_of_accounts");
			}
			return count;
		} catch (Exception e) {
			System.out.println("Failed to fetch number of accounts");
		}
		
		return 0;
	}
	
	public List<AccNumberType> getAccNumberTypesByCustomerId(int customerId) {
		String sql = """
				SELECT a.account_number, at.account_type 
				FROM customers c
				JOIN customer_accounts ca on c.customer_id = ca.customer_id
				JOIN accounts a on ca.account_id = a.account_id
				JOIN account_types at on a.account_type_id = at.account_type_id 
				where c.customer_id = ?
				""";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, customerId);
			var rs = stmt.executeQuery();
			List<AccNumberType> accNumberTypes = new ArrayList<>();
			while(rs.next()) {
				accNumberTypes.add(new AccNumberType(rs.getString("account_number"), rs.getString("account_type")));
			}
			return accNumberTypes;
		} catch (Exception e) {
			System.out.println("Failed to fetch accounts and types");
		}
		return null;
	}
	
	public int insertCustomer(Customer customer) {
		String sql = "INSERT INTO customers (name, nrc, dob, email, phone, address, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getNrc());
			stmt.setDate(3, Date.valueOf(customer.getDob()));
			stmt.setString(4, customer.getEmail());
			stmt.setString(5, customer.getPhone());
			stmt.setString(6, customer.getAddress());
			stmt.setInt(7, 3);
			int row = stmt.executeUpdate();
			return row;
		} catch (Exception e) {
			System.out.println("Failed to insert customer");
		}
		return 0;
	}
	
	public int updateCustomer(Customer customer) {
		String sql = "UPDATE customers SET name = ?, nrc = ?, dob = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getNrc());
			stmt.setDate(3, Date.valueOf(customer.getDob()));
			stmt.setString(4, customer.getEmail());
			stmt.setString(5, customer.getPhone());
			stmt.setString(6, customer.getAddress());
			stmt.setInt(7, customer.getId());
			int row = stmt.executeUpdate();
			return row;
		} catch (Exception e) {
			System.out.println("Failed to update customer");
		}
		return 0;
	}

	public Customer findCustomerByNRc(String nrc) {
		String sql = "SELECT * FROM customers WHERE nrc LIKE ?";

		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, "%" + nrc + "%");
			var rs = stmt.executeQuery();
			if(rs.next()) {
				Customer customer = new Customer();
				customer.setId(rs.getInt("customer_id"));
				customer.setName(rs.getString("name"));
				customer.setNrc(rs.getString("nrc"));
				customer.setDob(rs.getDate("dob").toLocalDate());
				customer.setEmail(rs.getString("email"));
				customer.setPhone(rs.getString("phone"));
				customer.setAddress(rs.getString("address"));
				customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				return customer;
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch customer by nrc");
		}
		return new Customer();
	}

}
