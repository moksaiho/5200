package game.model;

import java.util.Objects;

/**
 * Represents a gear item equipped by a character.
 */
public class EquippedItem {
    private Character character;
    private Gear.SlotType slot;
    private Gear gear;

    public EquippedItem(Character character, Gear.SlotType slot, Gear gear) {
        this.character = character;
        this.slot = slot;
        this.gear = gear;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Gear.SlotType getSlot() {
        return slot;
    }

    public void setSlot(Gear.SlotType slot) {
        this.slot = slot;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    /**
     * Check if the gear is in the correct slot
     */
    public boolean isValidSlot() {
        return gear.getSlot() == slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquippedItem that = (EquippedItem) o;
        return Objects.equals(character, that.character) && 
               slot == that.slot && 
               Objects.equals(gear, that.gear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, slot, gear);
    }

    @Override
    public String toString() {
        return String.format("EquippedItem(Character(%d), %s, Item(%d))", 
                character.getCharacterID(), slot, gear.getItemID());
    }
}