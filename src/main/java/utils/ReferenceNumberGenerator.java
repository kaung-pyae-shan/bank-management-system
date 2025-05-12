package utils;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import config.DatabaseConfig;

public class ReferenceNumberGenerator {

	public String generateNewReferenceNumber() throws ParseException {
		String lastReferenceNumber = getLastReferenceNumber();
		int newNumber;

		if (lastReferenceNumber != null) {
			// Assuming the reference number format is "REF-YYYYMMDD-XXXX"
			String[] parts = lastReferenceNumber.split("-");
			String lastDateString = parts[1]; // Extract the date part
			Date lastDate = new SimpleDateFormat("yyyyMMdd").parse(lastDateString);
			Date currentDate = new Date();

			// Format both dates to "yyyyMMdd" to compare only the date part
			String formattedLastDate = new SimpleDateFormat("yyyyMMdd").format(lastDate);
			String formattedCurrentDate = new SimpleDateFormat("yyyyMMdd").format(currentDate);

			// Check if the last record date is in the past
			if (formattedLastDate.compareTo(formattedCurrentDate) < 0) {
				newNumber = 1; // Start from 1 if the last date is in the past
			} else {
				newNumber = Integer.parseInt(parts[2]) + 1; // Increment the last number
			}
		} else {
			newNumber = 1; // Start from 1 if no previous reference number exists
		}

		// Generate the new reference number
		String newReferenceNumber = String.format("REF-%s-%04d", getCurrentDate(), newNumber);
		return newReferenceNumber;
	}

	private String getLastReferenceNumber() {
		String query = "SELECT reference_number FROM transactions WHERE transaction_date <= NOW() ORDER BY reference_number DESC LIMIT 1";
		try (var con = DatabaseConfig.getConnection();
				var stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getString("reference_number");
			}
		} catch (Exception e) {
			System.out.println("Failed to fetch last reference number");
		}
		return null; // No previous reference number found
	}

	private String getCurrentDate() {
		// Return the current date in the desired format (e.g., YYYYMMDD)
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
}