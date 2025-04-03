package game.dal;

import game.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDao {

    /**
     * Creates a new player record in the database.
     */
    public static Player create(Connection cxn, String fullName, String email) throws SQLException {
        String sql = "INSERT INTO Player (fullName, email) VALUES (?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int playerID = keys.getInt(1);
                    return new Player(playerID, fullName, email);
                } else {
                    throw new SQLException("Player creation failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Retrieves a player by ID.
     */
    public static Player getPlayerByID(Connection cxn, int playerID) throws SQLException {
        String sql = "SELECT * FROM Player WHERE playerID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, playerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String email = rs.getString("email");
                    return new Player(playerID, fullName, email);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves players by email domain (non-primary key example).
     * Example "additional" method #1.
     */
    public static List<Player> getPlayersByEmailDomain(Connection cxn, String domain) throws SQLException {
        String sql = "SELECT * FROM Player WHERE email LIKE ?";
        List<Player> players = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, "%" + domain);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("playerID");
                    String fullName = rs.getString("fullName");
                    String email = rs.getString("email");
                    players.add(new Player(id, fullName, email));
                }
            }
        }
        return players;
    }

    /**
     * Updates a player's email.
     * Example "additional" method #2.
     */
    public static Player updateEmail(Connection cxn, Player player, String newEmail) throws SQLException {
        String sql = "UPDATE Player SET email = ? WHERE playerID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, newEmail);
            stmt.setInt(2, player.getPlayerID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                player.setEmail(newEmail);
                return player;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a player record.
     * Example "additional" method #3.
     */
    public static void delete(Connection cxn, Player player) throws SQLException {
        String sql = "DELETE FROM Player WHERE playerID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, player.getPlayerID());
            stmt.executeUpdate();
        }
    }
}