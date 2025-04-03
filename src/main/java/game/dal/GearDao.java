package game.dal;

import game.model.Gear;
import game.model.Gear.GearType;
import game.model.Gear.SlotType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GearDao {

    /**
     * Creates a new gear item.
     */
    public static Gear create(Connection cxn, String itemName, int itemLevel, int maxStackSize, int itemPrice,
                              GearType gearType, SlotType slot, int requiredLevel, Integer damage) throws SQLException {

        // Insert into Item first
        String itemSql = "INSERT INTO Item (itemName, itemLevel, itemMaxStackSize, itemPrice) VALUES (?, ?, ?, ?)";
        int itemID;

        try (PreparedStatement stmt = cxn.prepareStatement(itemSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, itemLevel);
            stmt.setInt(3, maxStackSize);
            stmt.setInt(4, itemPrice);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    itemID = keys.getInt(1);
                } else {
                    throw new SQLException("Item creation failed, no ID returned.");
                }
            }
        }

        // Then insert into Gear
        String gearSql = "INSERT INTO Gear (itemID, gearType, slot, requiredLevel, damage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(gearSql)) {
            stmt.setInt(1, itemID);
            stmt.setString(2, gearType.name());
            stmt.setString(3, slot.name());
            stmt.setInt(4, requiredLevel);
            if (damage != null) {
                stmt.setInt(5, damage);
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.executeUpdate();
        }

        return new Gear(itemID, itemName, itemLevel, maxStackSize, itemPrice, gearType, slot, requiredLevel, damage);
    }

    /**
     * Retrieves a gear item by itemID.
     */
    public static Gear getGearByID(Connection cxn, int itemID) throws SQLException {
        String sql = """
            SELECT i.itemName, i.itemLevel, i.itemMaxStackSize, i.itemPrice,
                   g.gearType, g.slot, g.requiredLevel, g.damage
            FROM Item i JOIN Gear g ON i.itemID = g.itemID
            WHERE i.itemID = ?
        """;

        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("itemName");
                    int level = rs.getInt("itemLevel");
                    int stack = rs.getInt("itemMaxStackSize");
                    int price = rs.getInt("itemPrice");

                    GearType gearType = GearType.valueOf(rs.getString("gearType"));
                    SlotType slot = SlotType.valueOf(rs.getString("slot"));
                    int requiredLevel = rs.getInt("requiredLevel");
                    int damage = rs.getInt("damage");
                    if (rs.wasNull()) damage = -1;

                    return new Gear(itemID, name, level, stack, price, gearType, slot, requiredLevel, damage != -1 ? damage : null);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all gear by slot type.
     */
    public static List<Gear> getGearBySlot(Connection cxn, SlotType slot) throws SQLException {
        String sql = """
            SELECT i.itemID, i.itemName, i.itemLevel, i.itemMaxStackSize, i.itemPrice,
                   g.gearType, g.slot, g.requiredLevel, g.damage
            FROM Item i JOIN Gear g ON i.itemID = g.itemID
            WHERE g.slot = ?
        """;

        List<Gear> result = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, slot.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int itemID = rs.getInt("itemID");
                    String name = rs.getString("itemName");
                    int level = rs.getInt("itemLevel");
                    int stack = rs.getInt("itemMaxStackSize");
                    int price = rs.getInt("itemPrice");

                    GearType gearType = GearType.valueOf(rs.getString("gearType"));
                    SlotType s = SlotType.valueOf(rs.getString("slot"));
                    int requiredLevel = rs.getInt("requiredLevel");
                    int damage = rs.getInt("damage");
                    if (rs.wasNull()) damage = -1;

                    result.add(new Gear(itemID, name, level, stack, price, gearType, s, requiredLevel, damage != -1 ? damage : null));
                }
            }
        }
        return result;
    }

    /**
     * Updates required level of gear.
     */
    public static Gear updateRequiredLevel(Connection cxn, Gear gear, int newLevel) throws SQLException {
        String sql = "UPDATE Gear SET requiredLevel = ? WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, newLevel);
            stmt.setInt(2, gear.getItemID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                gear.setRequiredLevel(newLevel);
                return gear;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes the gear (from both Gear and Item tables).
     */
    public static void delete(Connection cxn, Gear gear) throws SQLException {
        // Delete from Gear
        String sql1 = "DELETE FROM Gear WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql1)) {
            stmt.setInt(1, gear.getItemID());
            stmt.executeUpdate();
        }

        // Delete from Item
        String sql2 = "DELETE FROM Item WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql2)) {
            stmt.setInt(1, gear.getItemID());
            stmt.executeUpdate();
        }
    }
}