package game.model;

import java.util.Objects;

/**
 * Represents an item in the game.
 */
public class Item {
    private int itemID;
    private String itemName;
    private int itemLevel;
    private int itemMaxStackSize;
    private int itemPrice;

    public Item(int itemID, String itemName, int itemLevel, int itemMaxStackSize, int itemPrice) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemLevel = itemLevel;
        this.itemMaxStackSize = itemMaxStackSize;
        this.itemPrice = itemPrice;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public int getItemMaxStackSize() {
        return itemMaxStackSize;
    }

    public void setItemMaxStackSize(int itemMaxStackSize) {
        this.itemMaxStackSize = itemMaxStackSize;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemID == item.itemID && 
               itemLevel == item.itemLevel && 
               itemMaxStackSize == item.itemMaxStackSize && 
               itemPrice == item.itemPrice && 
               Objects.equals(itemName, item.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID, itemName, itemLevel, itemMaxStackSize, itemPrice);
    }

    @Override
    public String toString() {
        return String.format("Item(%d, \"%s\", %d, %d, %d)", 
                itemID, itemName, itemLevel, itemMaxStackSize, itemPrice);
    }
} 