package game.dal;

import game.model.StatisticType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for the StatisticType table.
 * This table has a string 'statName' as a primary key (if that's how your schema is defined).
 */
public class StatisticTypeDao {

    /**
     * Creates a new StatisticType record in the database.
     */
    public static StatisticType create(Connection cxn, String statName, String description) throws SQLException {
        String sql = "INSERT INTO StatisticType (statName, description) VALUES (?, ?)";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, statName);
            ps.setString(2, description);
            ps.executeUpdate();
        }
        return new StatisticType(statName, description);
    }

    /**
     * Retrieves a StatisticType by its statName (primary key).
     */
    public static StatisticType getStatisticTypeByName(Connection cxn, String statName) throws SQLException {
        String sql = "SELECT statName, description FROM StatisticType WHERE statName = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, statName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String desc = rs.getString("description");
                    return new StatisticType(statName, desc);
                }
            }
        }
        return null;
    }

    /**
     * Example non-PK search: retrieve types by description (if relevant).
     */
    public static StatisticType getStatisticTypeByDescription(Connection cxn, String description) throws SQLException {
        String sql = "SELECT statName, description FROM StatisticType WHERE description = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, description);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String statName = rs.getString("statName");
                    return new StatisticType(statName, description);
                }
            }
        }
        return null;
    }

    /**
     * Updates the description of an existing StatisticType.
     */
    public static StatisticType updateDescription(Connection cxn, StatisticType type, String newDescription)
            throws SQLException {
        String sql = "UPDATE StatisticType SET description = ? WHERE statName = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, newDescription);
            ps.setString(2, type.getStatName());
            int updated = ps.executeUpdate();
            if (updated == 1) {
                // Update local object
                type.setDescription(newDescription);
                return type;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a StatisticType record from the database.
     */
    public static void delete(Connection cxn, StatisticType type) throws SQLException {
        String sql = "DELETE FROM StatisticType WHERE statName = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, type.getStatName());
            ps.executeUpdate();
        }
    }
}
