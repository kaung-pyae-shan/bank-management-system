package repository;

import java.sql.SQLException;

import config.DatabaseConfig;
import model.Staff.Role;

public class StaffRepository {

	public int findStaffIdByEmailandPassword(String email, String password) {
		String sql = "SELECT staff_id FROM staffs WHERE email = ? AND password = ?";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, password);
			var rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("staff_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Role findRoleByStaffId(int staffId) {
		String sql = "SELECT role FROM staffs WHERE staff_id = ?";
		try(var con = DatabaseConfig.getConnection(); var stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, staffId);
			var rs = stmt.executeQuery();
			if(rs.next()) {
				return Role.valueOf(rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
