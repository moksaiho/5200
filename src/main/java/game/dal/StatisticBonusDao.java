package game.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import game.model.Item;
import game.model.StatisticBonus;
import game.model.StatisticType;

public class StatisticBonusDao {
	/**
     * Creates a StatisticsBonus record (associates an item with a statistic type).
     */
    public static StatisticBonus create(Connection cxn, Item item, StatisticType statistic, String bonusType, Integer bonusFlatValue, Float bonusPercentageValue, Integer bonusCap) throws SQLException {
        String sql = "INSERT INTO StatisticBonus (itemID, statName, bonusType, bonusFlatValue, bonusPercentageValue, bonusCap) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, item.getItemID());
            stmt.setString(2, statistic.getStatName());
            stmt.setString(3, bonusType);
            
            if (bonusFlatValue == null) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, bonusFlatValue);
            }
            
            if (bonusPercentageValue == null) {
                stmt.setNull(5, java.sql.Types.FLOAT);
            } else {
                stmt.setFloat(5, bonusPercentageValue);
            }
            
            if (bonusCap == null) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(6, bonusCap);
            }
            
            stmt.executeUpdate();
            
            return new StatisticBonus(item, statistic, bonusType, bonusFlatValue, bonusPercentageValue, bonusCap);
        }
    }

    /**
     * Retrieves a Statistics bonus by item ID and statName.
     */
    public static StatisticBonus getByItemAndStatistics(Connection cxn, int itemID, String statName) throws SQLException {
        String sql = "SELECT * FROM StatisticBonus WHERE itemID = ? AND statName = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            stmt.setString(2, statName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String bonusType = rs.getString("bonusType");
                    
                    Integer bonusFlatValue = rs.getObject("bonusFlatValue", Integer.class);
                    Float bonusPercentageValue = rs.getObject("bonusPercentageValue", Float.class);
                    Integer bonusCap = rs.getObject("bonusCap", Integer.class);

                    Item item = ItemDao.getItemByID(cxn, itemID);
                    StatisticType statistics = StatisticTypeDao.getStatisticTypeByID(cxn, statName);

                    return new StatisticBonus(item, statistics, bonusType, bonusFlatValue, bonusPercentageValue, bonusCap);
                } else {
                    return null;
                }
            }
        }
    }


}
