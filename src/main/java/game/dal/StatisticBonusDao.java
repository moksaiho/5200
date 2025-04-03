package game.dal;

import game.model.Item;
import game.model.StatisticBonus;
import game.model.StatisticType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the StatisticBonus join table:
 * Associates an Item with a StatisticType and stores bonus info (flat, %).
 */
public class StatisticBonusDao {

    /**
     * Creates a new StatisticBonus record in the database.
     */
    public static StatisticBonus create(Connection cxn, Item item, StatisticType statType,
                                        String bonusType, Integer bonusFlatValue,
                                        Float bonusPercentageValue, Integer bonusCap)
            throws SQLException {
        String sql = """
            INSERT INTO StatisticBonus (itemID, statName, bonusType,
                                        bonusFlatValue, bonusPercentageValue, bonusCap)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, item.getItemID());
            ps.setString(2, statType.getStatName());
            ps.setString(3, bonusType);
            if (bonusFlatValue != null) {
                ps.setInt(4, bonusFlatValue);
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            if (bonusPercentageValue != null) {
                ps.setFloat(5, bonusPercentageValue);
            } else {
                ps.setNull(5, Types.FLOAT);
            }
            if (bonusCap != null) {
                ps.setInt(6, bonusCap);
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
        }
        return new StatisticBonus(item, statType, bonusType, bonusFlatValue, bonusPercentageValue, bonusCap);
    }

    /**
     * Retrieves a StatisticBonus by (itemID, statName, bonusType), the composite PK.
     */
    public static StatisticBonus getStatisticBonus(Connection cxn, int itemID, String statName, String bonusType)
            throws SQLException {
        String sql = """
            SELECT bonusFlatValue, bonusPercentageValue, bonusCap
            FROM StatisticBonus
            WHERE itemID = ? AND statName = ? AND bonusType = ?
            """;
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, itemID);
            ps.setString(2, statName);
            ps.setString(3, bonusType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Check for null columns
                    Integer flatValue = (rs.getObject("bonusFlatValue") != null)
                            ? rs.getInt("bonusFlatValue") : null;
                    Float pctValue = (rs.getObject("bonusPercentageValue") != null)
                            ? rs.getFloat("bonusPercentageValue") : null;
                    Integer bonusCap = (rs.getObject("bonusCap") != null)
                            ? rs.getInt("bonusCap") : null;

                    Item item = ItemDao.getItemByID(cxn, itemID);
                    StatisticType st = StatisticTypeDao.getStatisticTypeByName(cxn, statName);
                    if (item != null && st != null) {
                        return new StatisticBonus(item, st, bonusType,
                                flatValue, pctValue, bonusCap);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all StatisticBonus entries for a specific item (non-PK search).
     */
    public static List<StatisticBonus> getBonusesByItem(Connection cxn, Item item) throws SQLException {
        List<StatisticBonus> results = new ArrayList<>();
        String sql = "SELECT statName, bonusType, bonusFlatValue, bonusPercentageValue, bonusCap FROM StatisticBonus WHERE itemID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, item.getItemID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String statName = rs.getString("statName");
                    String bonusType = rs.getString("bonusType");

                    Integer flatValue = (rs.getObject("bonusFlatValue") != null)
                            ? rs.getInt("bonusFlatValue") : null;
                    Float pctValue = (rs.getObject("bonusPercentageValue") != null)
                            ? rs.getFloat("bonusPercentageValue") : null;
                    Integer bonusCap = (rs.getObject("bonusCap") != null)
                            ? rs.getInt("bonusCap") : null;

                    StatisticType st = StatisticTypeDao.getStatisticTypeByName(cxn, statName);
                    if (st != null) {
                        results.add(new StatisticBonus(item, st, bonusType, flatValue, pctValue, bonusCap));
                    }
                }
            }
        }
        return results;
    }

    /**
     * Example update method: changes the flat bonus value.
     */
    public static StatisticBonus updateFlatValue(Connection cxn, StatisticBonus sb, Integer newFlatValue)
            throws SQLException {
        String sql = """
            UPDATE StatisticBonus
            SET bonusFlatValue = ?
            WHERE itemID = ? AND statName = ? AND bonusType = ?
            """;
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            if (newFlatValue == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, newFlatValue);
            }
            ps.setInt(2, sb.getItem().getItemID());
            ps.setString(3, sb.getStatisticType().getStatName());
            ps.setString(4, sb.getBonusType());
            int updated = ps.executeUpdate();
            if (updated == 1) {
                sb.setBonusFlatValue(newFlatValue);
                return sb;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a StatisticBonus record from the database.
     */
    public static void delete(Connection cxn, StatisticBonus sb) throws SQLException {
        String sql = "DELETE FROM StatisticBonus WHERE itemID = ? AND statName = ? AND bonusType = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, sb.getItem().getItemID());
            ps.setString(2, sb.getStatisticType().getStatName());
            ps.setString(3, sb.getBonusType());
            ps.executeUpdate();
        }
    }
}
