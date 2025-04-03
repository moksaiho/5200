package dao;

import java.sql.*;

import model.Currency;


public class CurrencyDao {
	/**
     * Creates a new currency in the database.
     */
    public static Currency create(Connection cxn, String currencyName, Integer cap, Integer weeklyCap) throws SQLException {
        String sql = "INSERT INTO Currency (currencyName, cap, weeklyCap) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, currencyName);
            stmt.setInt(2, cap);
            stmt.setInt(3, weeklyCap);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int currencyID = keys.getInt(1);
                    return new Currency(currencyID, currencyName, cap, weeklyCap);
                } else {
                    throw new SQLException("Character creation failed, no ID returned.");
                }
            }
        }
    }

    /**
     * Retrieves a currency by its ID.
     */
    public static Currency getCurrencyByID(Connection cxn, int currencyID) throws SQLException {
    	String sql = "SELECT * FROM Currency WHERE currencyID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, currencyID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String currencyName = rs.getString("currencyName");
                    Integer cap = rs.getInt("cap");
                    Integer weeklyCap = rs.getInt("weeklyCap");
                    return new Currency(currencyID, currencyName, cap, weeklyCap);
                } else {
                    return null;
                }
            }
        }
    }

}
