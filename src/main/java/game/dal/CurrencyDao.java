package game.dal;

import java.sql.*;

import game.model.Currency;


public class CurrencyDao {
	/**
     * Creates a new currency in the database.
     */
    public static Currency create(Connection cxn, String currencyName, Integer cap, Integer weeklyCap) throws SQLException {
        String sql = "INSERT INTO Currency (currencyName, cap, weeklyCap) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, currencyName);
            
            if (cap == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, cap);
            }
            
            if (weeklyCap == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, weeklyCap);
            }
            
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int currencyID = keys.getInt(1);
                    return new Currency(currencyID, currencyName, cap, weeklyCap);
                } else {
                    throw new SQLException("Currency creation failed, no ID returned.");
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
                    
                    Integer cap = rs.getObject("cap", Integer.class);
                    Integer weeklyCap = rs.getObject("weeklyCap", Integer.class);

                    return new Currency(currencyID, currencyName, cap, weeklyCap);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Updates the weekly cap of a currency.
     */
    public static Currency updateWeeklyCap(Connection cxn, Currency currency, Integer newWeeklyCap) throws SQLException {
        String sql = "UPDATE Currency SET weeklyCap = ? WHERE currencyID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            if (newWeeklyCap == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, newWeeklyCap);
            }
            
            stmt.setInt(2, currency.getCurrencyID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                currency.setWeeklyCap(newWeeklyCap);
                return currency;
            } else {
                return null;
            }
        }
    }
}
