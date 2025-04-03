package game.dal;

import game.model.Character;
import game.model.CharacterStatistics;
import game.model.StatisticType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the CharacterStatistics table (join between Character and StatisticType).
 */
public class CharacterStatisticsDao {

    /**
     * Creates a new record in CharacterStatistics.
     */
    public static CharacterStatistics create(Connection cxn, Character character,
                                             StatisticType statType, int statValue)
            throws SQLException {
        String sql = "INSERT INTO CharacterStatistics (characterID, statName, statValue) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            ps.setString(2, statType.getStatName());
            ps.setInt(3, statValue);
            ps.executeUpdate();
        }
        return new CharacterStatistics(character, statType, statValue);
    }

    /**
     * Retrieves a CharacterStatistics record by (characterID, statName).
     */
    public static CharacterStatistics getByCharacterAndStat(Connection cxn, int characterID, String statName)
            throws SQLException {
        String sql = "SELECT statValue FROM CharacterStatistics WHERE characterID = ? AND statName = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, characterID);
            ps.setString(2, statName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int statValue = rs.getInt("statValue");
                    // Retrieve associated objects
                    Character ch = CharacterDao.getCharacterByID(cxn, characterID);
                    StatisticType st = StatisticTypeDao.getStatisticTypeByName(cxn, statName);
                    if (ch != null && st != null) {
                        return new CharacterStatistics(ch, st, statValue);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all CharacterStatistics for a given Character (non-PK search).
     */
    public static List<CharacterStatistics> getByCharacter(Connection cxn, Character character) throws SQLException {
        List<CharacterStatistics> list = new ArrayList<>();
        String sql = "SELECT statName, statValue FROM CharacterStatistics WHERE characterID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String statName = rs.getString("statName");
                    int statValue = rs.getInt("statValue");
                    StatisticType st = StatisticTypeDao.getStatisticTypeByName(cxn, statName);
                    if (st != null) {
                        list.add(new CharacterStatistics(character, st, statValue));
                    }
                }
            }
        }
        return list;
    }

    /**
     * Updates the statValue for a CharacterStatistics record.
     */
    public static CharacterStatistics updateStatValue(Connection cxn, CharacterStatistics cs, int newValue)
            throws SQLException {
        String sql = "UPDATE CharacterStatistics SET statValue = ? WHERE characterID = ? AND statName = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, newValue);
            ps.setInt(2, cs.getCharacter().getCharacterID());
            ps.setString(3, cs.getStatisticType().getStatName());
            int updated = ps.executeUpdate();
            if (updated == 1) {
                cs.setStatValue(newValue);
                return cs;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a CharacterStatistics record from the database.
     */
    public static void delete(Connection cxn, CharacterStatistics cs) throws SQLException {
        String sql = "DELETE FROM CharacterStatistics WHERE characterID = ? AND statName = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, cs.getCharacter().getCharacterID());
            ps.setString(2, cs.getStatisticType().getStatName());
            ps.executeUpdate();
        }
    }
}
