package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.StatisticType;

public class StatisticTypeDao {
	/**
     * Creates a new StatisticType item.
     */
    public static StatisticType create(Connection cxn, String statName, String description) throws SQLException {
        String sql = "INSERT INTO StatisticType (statName, description) VALUES (?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, statName);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }

        return new StatisticType(statName, description);
    }

    /**
     * Retrieves a statistic type by statistic name.
     */
    public static StatisticType getStatisticTypeByID(Connection cxn, String statName) throws SQLException {
        String sql = "SELECT * FROM StatisticType WHERE statName = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, statName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String desc = rs.getString("description");

                    return new StatisticType(statName, desc);
                } else {
                    return null;
                }
            }
        }
    }

}
