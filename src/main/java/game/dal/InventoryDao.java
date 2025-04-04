package game.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import game.model.Character;

import game.model.Inventory;
import game.model.Item;


public class InventoryDao {
	/**
     * Creates an inventory record (associates a character with a slotNumber).
     */
    public static Inventory create(Connection cxn, Character character, int slotNumber, Item item, int quantity) throws SQLException {
        String sql = "INSERT INTO Inventory (characterID, slotNumber, itemID, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setInt(2, slotNumber);
            stmt.setInt(3, item.getItemID());
            stmt.setInt(4, quantity);
            
            stmt.executeUpdate();
            return new Inventory(character, slotNumber, item, quantity);
        }
    }

    /**
     * Retrieves an inventory by character ID and slotNumber.
     */
    public static Inventory getByCharacterAndSlotNumber(Connection cxn, int characterID, int slotNumber) throws SQLException {
        String sql = "SELECT * FROM Inventory WHERE characterID = ? AND slotNumber = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            stmt.setInt(2, slotNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int itemID = rs.getInt("itemID");
                    int quantity = rs.getInt("quantity");

                    Character character = CharacterDao.getCharacterByID(cxn, characterID);
                    Item item = ItemDao.getItemByID(cxn, itemID);
                                        
                    return new Inventory(character, slotNumber, item, quantity);
                } else {
                    return null;
                }
            }
        }
    }
}
