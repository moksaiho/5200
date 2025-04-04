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

public class EquippedItemDao {
	/**
     * Creates an equipped item record (associates a character with a slot type).
     */
    public static EquippedItem create(Connection cxn, Character character, SlotType slot, Gear gear) throws SQLException {
        String sql = "INSERT INTO EquippedItem (characterID, slot, itemID) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setString(2, slot.name());
            stmt.setInt(3, gear.getItemID());
           
            stmt.executeUpdate();
            return new EquippedItem(character, slot, gear);
        }
    }

    /**
     * Retrieves an equipped item by character ID and slot type.
     */
    public static EquippedItem getByCharacterAndSlotType(Connection cxn, int characterID, SlotType slot) throws SQLException {
        String sql = "SELECT * FROM EquippedItem WHERE characterID = ? AND slot = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            stmt.setString(2, slot.name());
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
}
