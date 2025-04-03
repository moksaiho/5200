package game.dal;

import game.model.Race;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceDao {

    /**
     * Creates a new race record in the database.
     */
    public static Race create(Connection cxn, String raceName) throws SQLException {
        String sql = "INSERT INTO Race (raceName) VALUES (?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, raceName);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int raceID = keys.getInt(1);
                    return new Race(raceID, raceName);
                } else {
                    throw new SQLException("Race creation failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Retrieves a race by its ID.
     */
    public static Race getRaceByID(Connection cxn, int raceID) throws SQLException {
        String sql = "SELECT * FROM Race WHERE raceID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, raceID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("raceName");
                    return new Race(raceID, name);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all races with a given name (example of "search by non-PK").
     */
    public static List<Race> getRacesByName(Connection cxn, String raceName) throws SQLException {
        String sql = "SELECT * FROM Race WHERE raceName = ?";
        List<Race> races = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, raceName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int raceID = rs.getInt("raceID");
                    races.add(new Race(raceID, raceName));
                }
            }
        }
        return races;
    }

    /**
     * Updates the name of a race.
     */
    public static Race updateRaceName(Connection cxn, Race race, String newName) throws SQLException {
        String sql = "UPDATE Race SET raceName = ? WHERE raceID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, race.getRaceID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                race.setRaceName(newName);
                return race;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a race from the database.
     */
    public static void delete(Connection cxn, Race race) throws SQLException {
        String sql = "DELETE FROM Race WHERE raceID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, race.getRaceID());
            stmt.executeUpdate();
        }
    }
}