package game.dal;

import game.model.Character;
import game.model.Player;
import game.model.Clan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterDao {

    /**
     * Creates a new character in the database.
     */
    public static Character create(Connection cxn, Player player, Clan clan, String firstName, String lastName) throws SQLException {
        String sql = "INSERT INTO Character (playerID, clanID, firstName, lastName) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, player.getPlayerID());
            stmt.setInt(2, clan.getClanID());
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int characterID = keys.getInt(1);
                    return new Character(characterID, player, clan, firstName, lastName);
                } else {
                    throw new SQLException("Character creation failed, no ID returned.");
                }
            }
        }
    }

    /**
     * Retrieves a character by its ID.
     */
    public static Character getCharacterByID(Connection cxn, int characterID) throws SQLException {
        String sql = "SELECT * FROM Character WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int playerID = rs.getInt("playerID");
                    int clanID = rs.getInt("clanID");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");

                    Player player = PlayerDao.getPlayerByID(cxn, playerID);
                    Clan clan = ClanDao.getClanByID(cxn, clanID);

                    return new Character(characterID, player, clan, firstName, lastName);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves characters by last name (non-PK search).
     */
    public static List<Character> getCharactersByLastName(Connection cxn, String lastName) throws SQLException {
        String sql = "SELECT * FROM Character WHERE lastName = ?";
        List<Character> characters = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int characterID = rs.getInt("characterID");
                    int playerID = rs.getInt("playerID");
                    int clanID = rs.getInt("clanID");
                    String firstName = rs.getString("firstName");

                    Player player = PlayerDao.getPlayerByID(cxn, playerID);
                    Clan clan = ClanDao.getClanByID(cxn, clanID);

                    characters.add(new Character(characterID, player, clan, firstName, lastName));
                }
            }
        }
        return characters;
    }

    /**
     * Updates a character's first name.
     */
    public static Character updateFirstName(Connection cxn, Character character, String newFirstName) throws SQLException {
        String sql = "UPDATE Character SET firstName = ? WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, newFirstName);
            stmt.setInt(2, character.getCharacterID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                character.setFirstName(newFirstName);
                return character;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a character.
     */
    public static void delete(Connection cxn, Character character) throws SQLException {
        String sql = "DELETE FROM Character WHERE characterID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.executeUpdate();
        }
    }
}