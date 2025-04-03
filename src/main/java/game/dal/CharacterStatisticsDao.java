package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Character;
import model.CharacterStatistics;
import model.StatisticType;

public class CharacterStatisticsDao {

	/**
     * Creates a CharacterStatistics record (associates a character with a statistic).
     */
    public static CharacterStatistics create(Connection cxn, Character character, StatisticType statistics, int statValue) throws SQLException {
        String sql = "INSERT INTO CharacterStatistics (characterID, statName, statValue) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setString(2, statistics.getStatName());
            stmt.setInt(3, statValue);
            stmt.executeUpdate();
            return new CharacterStatistics(character, statistics, statValue);
        }
    }

    /**
     * Retrieves a CharacterStatistics by character ID and statName.
     */
    public static CharacterStatistics getByCharacterAndStatistics(Connection cxn, int characterID, String statName) throws SQLException {
        String sql = "SELECT * FROM CharacterStatistics WHERE characterID = ? AND statName = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            stmt.setString(2, statName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int statValue = rs.getInt("statValue");

                    Character character = CharacterDao.getCharacterByID(cxn, characterID);
                    StatisticType statistics = StatisticTypeDao.getStatisticTypeByID(cxn, statName);

                    return new CharacterStatistics(character, statistics, statValue);
                } else {
                    return null;
                }
            }
        }
    }


}
