package game.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import game.model.Character;
import game.model.EquippedItem;
import game.model.Gear;
import game.model.Gear.SlotType;
import game.model.Item;

import java.util.ArrayList;
import java.util.List;

public class EquippedItemDao {
	/**
     * Creates a new equipped item record in the database.
     */
    public static EquippedItem create(Connection cxn, Character character, SlotType slot, Gear gear) throws SQLException {
        String sql = "INSERT INTO EquippedItem (characterID, slot, itemID) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setString(2, slot.toString());
            stmt.setInt(3, gear.getItemID());
            stmt.executeUpdate();
            return new EquippedItem(character, slot, gear);
        }
    }

    /**
     * Retrieves an equipped item by character ID and slot.
     */
    public static EquippedItem getEquippedItemByCharacterAndSlot(Connection cxn, int characterID, SlotType slot) throws SQLException {
        String sql = "SELECT * FROM EquippedItem WHERE characterID = ? AND slot = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            stmt.setString(2, slot.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int itemID = rs.getInt("itemID");
                    
                    Character character = CharacterDao.getCharacterByID(cxn, characterID);
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    
                    return new EquippedItem(character, slot, gear);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all equipped items for a character.
     */
    public static List<EquippedItem> getEquippedItemsByCharacter(Connection cxn, Character character) throws SQLException {
        String sql = "SELECT * FROM EquippedItem WHERE characterID = ?";
        List<EquippedItem> equippedItems = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String slotStr = rs.getString("slot");
                    int itemID = rs.getInt("itemID");
                    
                    SlotType slot = SlotType.valueOf(slotStr);
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    
                    equippedItems.add(new EquippedItem(character, slot, gear));
                }
            }
        }
        return equippedItems;
    }

    /**
     * Updates an equipped item.
     */
    public static EquippedItem update(Connection cxn, Character character, SlotType slot, Gear newGear) throws SQLException {
        String sql = "UPDATE EquippedItem SET itemID = ? WHERE characterID = ? AND slot = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, newGear.getItemID());
            stmt.setInt(2, character.getCharacterID());
            stmt.setString(3, slot.toString());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                return new EquippedItem(character, slot, newGear);
            } else {
                return null;
            }
        }
    }

    /**
     * Removes an item from the specified slot for a character.
     */
    public static void remove(Connection cxn, Character character, SlotType slot) throws SQLException {
        String sql = "DELETE FROM EquippedItem WHERE characterID = ? AND slot = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setString(2, slot.toString());
            stmt.executeUpdate();
        }
    }
}
