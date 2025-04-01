package game.model;

import java.util.Objects;

/**
 * Represents a consumable item in the game.
 */
public class Consumable extends Item {
    private String description;

    public Consumable(int itemID, String itemName, int itemLevel, int itemMaxStackSize, 
                      int itemPrice, String description) {
        super(itemID, itemName, itemLevel, itemMaxStackSize, itemPrice);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Consumable that = (Consumable) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }

    @Override
    public String toString() {
        return String.format("Consumable(%s, \"%s\")", 
                super.toString(), description);
    }
} 