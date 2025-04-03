package game.dal;

import game.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    /**
     * Creates a new item in the database.
     */
    public static Item create(Connection cxn, String itemName, int itemLevel, int maxStackSize, int itemPrice) throws SQLException {
        String sql = "INSERT INTO Item (itemName, itemLevel, itemMaxStackSize, itemPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, itemLevel);
            stmt.setInt(3, maxStackSize);
            stmt.setInt(4, itemPrice);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int itemID = keys.getInt(1);
                    return new Item(itemID, itemName, itemLevel, maxStackSize, itemPrice);
                } else {
                    throw new SQLException("Item creation failed, no ID returned.");
                }
            }
        }
    }

    /**
     * Retrieves an item by its ID.
     */
    public static Item getItemByID(Connection cxn, int itemID) throws SQLException {
        String sql = "SELECT * FROM Item WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("itemName");
                    int level = rs.getInt("itemLevel");
                    int stack = rs.getInt("itemMaxStackSize");
                    int price = rs.getInt("itemPrice");
                    return new Item(itemID, name, level, stack, price);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves all items at a given level.
     */
    public static List<Item> getItemsByLevel(Connection cxn, int level) throws SQLException {
        String sql = "SELECT * FROM Item WHERE itemLevel = ?";
        List<Item> items = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, level);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("itemID");
                    String name = rs.getString("itemName");
                    int stack = rs.getInt("itemMaxStackSize");
                    int price = rs.getInt("itemPrice");
                    items.add(new Item(id, name, level, stack, price));
                }
            }
        }
        return items;
    }

    /**
     * Updates an item's price.
     */
    public static Item updateItemPrice(Connection cxn, Item item, int newPrice) throws SQLException {
        String sql = "UPDATE Item SET itemPrice = ? WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, newPrice);
            stmt.setInt(2, item.getItemID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                item.setItemPrice(newPrice);
                return item;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes an item.
     */
    public static void delete(Connection cxn, Item item) throws SQLException {
        String sql = "DELETE FROM Item WHERE itemID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, item.getItemID());
            stmt.executeUpdate();
        }
    }
}