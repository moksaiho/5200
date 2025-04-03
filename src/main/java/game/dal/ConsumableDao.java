package game.dal;

import game.model.Consumable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumableDao {

    /**
     * Creates a new consumable item.
     */
    public static Consumable create(Connection cxn, String itemName, int itemLevel, int maxStackSize, int itemPrice, String description) throws SQLException {
        // First insert into Item table
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

        // Then insert into Consumable table
        String consumableSql = "INSERT INTO Consumable (itemID, description) VALUES (?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(consumableSql)) {
            stmt.setInt(1, itemID);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }

        return new Consumable(itemID, itemName, itemLevel, maxStackSize, itemPrice, description);
    }

    /**
     * Retrieves a consumable by item ID.
     */
    public static Consumable getConsumableByID(Connection cxn, int itemID) throws SQLException {
        String sql = "SELECT i.itemName, i.itemLevel, i.itemMaxStackSize, i.itemPrice, c.description " +
                     "FROM Item i JOIN Consumable c ON i.itemID = c.itemID WHERE i.itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("itemName");
                    int level = rs.getInt("itemLevel");
                    int stack = rs.getInt("itemMaxStackSize");
                    int price = rs.getInt("itemPrice");
                    String desc = rs.getString("description");

                    return new Consumable(itemID, name, level, stack, price, desc);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all consumables with a given description.
     */
    public static List<Consumable> getConsumablesByDescription(Connection cxn, String desc) throws SQLException {
        String sql = "SELECT i.itemID, i.itemName, i.itemLevel, i.itemMaxStackSize, i.itemPrice, c.description " +
                     "FROM Item i JOIN Consumable c ON i.itemID = c.itemID WHERE c.description = ?";
        List<Consumable> result = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, desc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int itemID = rs.getInt("itemID");
                    String name = rs.getString("itemName");
                    int level = rs.getInt("itemLevel");
                    int stack = rs.getInt("itemMaxStackSize");
                    int price = rs.getInt("itemPrice");
                    result.add(new Consumable(itemID, name, level, stack, price, desc));
                }
            }
        }
        return result;
    }

    /**
     * Updates the description of a consumable.
     */
    public static Consumable updateDescription(Connection cxn, Consumable consumable, String newDesc) throws SQLException {
        String sql = "UPDATE Consumable SET description = ? WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, newDesc);
            stmt.setInt(2, consumable.getItemID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                consumable.setDescription(newDesc);
                return consumable;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes the consumable (from both Consumable and Item tables).
     */
    public static void delete(Connection cxn, Consumable consumable) throws SQLException {
        // First delete from Consumable
        String sql1 = "DELETE FROM Consumable WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql1)) {
            stmt.setInt(1, consumable.getItemID());
            stmt.executeUpdate();
        }

        // Then delete from Item
        String sql2 = "DELETE FROM Item WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql2)) {
            stmt.setInt(1, consumable.getItemID());
            stmt.executeUpdate();
        }
    }
}