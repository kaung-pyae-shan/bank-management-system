package repository;

import config.DatabaseConfig;

import java.sql.*;

public class UserRepository {

	// Method to save staff data into the database
	public static void saveStaff(String username, String password, String email, String phone, String role) {
		String sql = "INSERT INTO staffs (username, password, email, phone, role) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, email);
			stmt.setString(4, phone);
			stmt.setString(5, role);

			stmt.executeUpdate();
			System.out.println("Staff saved successfully.");
		} catch (SQLException e) {
			System.err.println("Error saving staff: " + e.getMessage());
		}
	}

	public static boolean updateStaff(int staffId, String username, String email, String phone, String role)
			throws SQLException {
		String sql = "UPDATE staffs SET username=?, email=?, phone=?, role=? WHERE staff_id=?";
		try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, email);
			stmt.setString(3, phone);
			stmt.setString(4, role);
			stmt.setInt(5, staffId);
			return stmt.executeUpdate() > 0;
		}
	}

	public static void deleteStaff(int staffId) {
		String sql = "DELETE FROM staffs WHERE staff_id = ?";

		try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, staffId);
			int rowsDeleted = stmt.executeUpdate();

			if (rowsDeleted > 0) {
				System.out.println("Staff deleted successfully.");
			} else {
				System.out.println("No staff found with the provided staff_id.");
			}
		} catch (SQLException e) {
			System.err.println("Error deleting staff: " + e.getMessage());
		}
	}

	// Method to retrieve all staff from the database
	public static ResultSet getAllStaff() {
		String sql = "SELECT staff_id, username, password, email, phone, role FROM staffs";

		try {
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			return stmt.executeQuery();
		} catch (SQLException e) {
			System.err.println("Error fetching staff: " + e.getMessage());
		}
		return null;
	}

	public static ResultSet searchStaff(String keyword) {
		String sql = "SELECT staff_id, username, password, email, phone, role FROM staffs "
				+ "WHERE username LIKE ? OR email LIKE ? OR phone LIKE ? OR role LIKE ?";

	    try {
	        Connection conn = DatabaseConfig.getConnection();

	        // Check if keyword is a number (for exact staff_id search)
	        try {
	            int staffId = Integer.parseInt(keyword);
	            sql = "SELECT staff_id, username, email, phone, role FROM staffs WHERE staff_id = ?";
	            stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, staffId);
	        } catch (NumberFormatException e) {
	            // Not a number → search by other fields using LIKE
	            sql = "SELECT staff_id, username, email, phone, role FROM staffs " +
	                  "WHERE username LIKE ? OR email LIKE ? OR phone LIKE ? OR role LIKE ?";
	            stmt = conn.prepareStatement(sql);
	            String likeKeyword = "%" + keyword + "%";
	            stmt.setString(1, likeKeyword);
	            stmt.setString(2, likeKeyword);
	            stmt.setString(3, likeKeyword);
	            stmt.setString(4, likeKeyword);
	        }

	        return stmt.executeQuery();
	    } catch (SQLException e) {
	        System.err.println("Error searching staff: " + e.getMessage());
	        return null;
	    }
	}


}
