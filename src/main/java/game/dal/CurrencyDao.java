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
            if (cap != null) {
                stmt.setInt(2, cap);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (weeklyCap != null) {
                stmt.setInt(3, weeklyCap);
            } else {
                stmt.setNull(3, Types.INTEGER);
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
                    Integer cap = rs.getInt("cap");
                    if (rs.wasNull()) {
                        cap = null;
                    }
                    Integer weeklyCap = rs.getInt("weeklyCap");
                    if (rs.wasNull()) {
                        weeklyCap = null;
                    }
                    return new Currency(currencyID, currencyName, cap, weeklyCap);
                } else {
                    return null;
                }
            }
        }
    }

}
