package game.dal;

import game.model.Character;
import game.model.EquippedItem;
import game.model.Gear;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the EquippedItem table.
 * Each record associates a Character, a Gear slot, and a Gear item.
 */
public class EquippedItemDao {

    /**
     * Creates a new EquippedItem record in the database.
     */
    public static EquippedItem create(Connection cxn, Character character, Gear.SlotType slot, Gear gear)
            throws SQLException {
        String sql = "INSERT INTO EquippedItem (characterID, slot, itemID) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            ps.setString(2, slot.name());
            ps.setInt(3, gear.getItemID());
            ps.executeUpdate();
        }
        // Return the new in-memory EquippedItem
        return new EquippedItem(character, slot, gear);
    }

    /**
     * Retrieves an EquippedItem by character ID and slot (its primary key).
     */
    public static EquippedItem getByCharacterAndSlot(Connection cxn, int characterID, Gear.SlotType slot)
            throws SQLException {
        String sql = "SELECT itemID FROM EquippedItem WHERE characterID = ? AND slot = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, characterID);
            ps.setString(2, slot.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int itemID = rs.getInt("itemID");

                    // Fetch the associated Character and Gear via existing DAOs
                    game.model.Character charObj = CharacterDao.getCharacterByID(cxn, characterID);
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    if (charObj != null && gear != null) {
                        return new EquippedItem(charObj, slot, gear);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all EquippedItems for a given character (non-PK search).
     */
    public static List<EquippedItem> getByCharacter(Connection cxn, Character character) throws SQLException {
        List<EquippedItem> results = new ArrayList<>();
        String sql = "SELECT * FROM EquippedItem WHERE characterID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, character.getCharacterID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Gear.SlotType slot = Gear.SlotType.valueOf(rs.getString("slot"));
                    int itemID = rs.getInt("itemID");
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    if (gear != null) {
                        results.add(new EquippedItem(character, slot, gear));
                    }
                }
            }
        }
        return results;
    }

    /**
     * Updates the itemID for an EquippedItem (i.e. which gear is in the slot).
     */
    public static EquippedItem updateEquippedGear(Connection cxn, EquippedItem eq, Gear newGear) throws SQLException {
        String sql = "UPDATE EquippedItem SET itemID = ? WHERE characterID = ? AND slot = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, newGear.getItemID());
            ps.setInt(2, eq.getCharacter().getCharacterID());
            ps.setString(3, eq.getSlot().name());
            int updated = ps.executeUpdate();
            if (updated == 1) {
                // Update in-memory object
                eq.setGear(newGear
