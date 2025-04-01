package game.model;

import java.util.Objects;

/**
 * Represents an item in a character's inventory.
 */
public class Inventory {
    private Character character;
    private int slotNumber;
    private Item item;
    private int quantity;

    public Inventory(Character character, int slotNumber, Item item, int quantity) {
        this.character = character;
        this.slotNumber = slotNumber;
        this.item = item;
        this.quantity = quantity;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Check if this inventory slot is full (quantity equals max stack size)
     */
    public boolean isFull() {
        return quantity >= item.getItemMaxStackSize();
    }

    /**
     * Calculate how many more of this item can be added to this stack
     */
    public int getRemainingCapacity() {
        return Math.max(0, item.getItemMaxStackSize() - quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return slotNumber == inventory.slotNumber && 
               quantity == inventory.quantity && 
               Objects.equals(character, inventory.character) && 
               Objects.equals(item, inventory.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, slotNumber, item, quantity);
    }

    @Override
    public String toString() {
        return String.format("Inventory(Character(%d), %d, Item(%d), %d)", 
                character.getCharacterID(), slotNumber, item.getItemID(), quantity);
    }
} 