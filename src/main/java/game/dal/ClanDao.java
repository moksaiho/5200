package game.dal;

import game.model.Clan;
import game.model.Race;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClanDao {

    /**
     * Creates a new clan in the database.
     */
    public static Clan create(Connection cxn, String clanName, Race race) throws SQLException {
        String sql = "INSERT INTO Clan (clanName, raceID) VALUES (?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, clanName);
            stmt.setInt(2, race.getRaceID());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int clanID = keys.getInt(1);
                    return new Clan(clanID, clanName, race);
                } else {
                    throw new SQLException("Clan creation failed, no ID returned.");
                }
            }
        }
    }

    /**
     * Retrieves a clan by its ID.
     */
    public static Clan getClanByID(Connection cxn, int clanID) throws SQLException {
        String sql = "SELECT * FROM Clan WHERE clanID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, clanID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String clanName = rs.getString("clanName");
                    int raceID = rs.getInt("raceID");
                    Race race = RaceDao.getRaceByID(cxn, raceID);
                    return new Clan(clanID, clanName, race);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all clans for a given race (non-PK search example).
     */
    public static List<Clan> getClansByRace(Connection cxn, Race race) throws SQLException {
        String sql = "SELECT * FROM Clan WHERE raceID = ?";
        List<Clan> clans = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, race.getRaceID());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int clanID = rs.getInt("clanID");
                    String clanName = rs.getString("clanName");
                    clans.add(new Clan(clanID, clanName, race));
                }
            }
        }
        return clans;
    }

    /**
     * Updates the clan name.
     */
    public static Clan updateClanName(Connection cxn, Clan clan, String newName) throws SQLException {
        String sql = "UPDATE Clan SET clanName = ? WHERE clanID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, clan.getClanID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                clan.setClanName(newName);
                return clan;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a clan.
     */
    public static void delete(Connection cxn, Clan clan) throws SQLException {
        String sql = "DELETE FROM Clan WHERE clanID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, clan.getClanID());
            stmt.executeUpdate();
        }
    }
}