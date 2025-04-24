package repository;

import config.DatabaseConfig;

public class CardRepository {

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
}
