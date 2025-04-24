package repository;

import config.DatabaseConfig;

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

}
